package com.example.sporthub.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.hardware.camera2.CaptureRequest
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ml.PoseClassifier
import ml.PoseSample
import ml.RepetitionCounter

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null

    private val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
        .build()

    private val faceOptions = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val poseDetector = PoseDetection.getClient(options)
    private val faceDetector = FaceDetection.getClient(faceOptions)

    private val _detectedPose = MutableStateFlow<Pose?>(null)
    val detectedPose = _detectedPose.asStateFlow()

    private val _detectedFace = MutableStateFlow<List<Face>>(emptyList())
    val detectorFaces = _detectedFace.asStateFlow()

    private var poseClassifier: PoseClassifier? = null
    private val repetitionCounter = mutableListOf<RepetitionCounter>()

    private val _classificationResult = MutableStateFlow("")
    val classificationResult = _classificationResult.asStateFlow()

    private val _classificationEnd = MutableStateFlow("")
    val classificationEnd = _classificationEnd.asStateFlow()

    private val _active = MutableStateFlow(false)

    private val _frontCamera = MutableStateFlow(false)

    private val _faceCamera = MutableStateFlow(false)
    val frontCamera = _frontCamera.asStateFlow()

    var imageCapture: ImageCapture? = null

    private val context = getApplication<Application>()

    @OptIn(ExperimentalCamera2Interop::class)
    fun bindToCamera(
        lifecycleOwner: LifecycleOwner,
        surfaceProvider: Preview.SurfaceProvider
    ) {
        viewModelScope.launch {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(context.applicationContext).await()

                val cameraSelector = if(_frontCamera.value) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                val cameraInfo = cameraProvider?.getCameraInfo(cameraSelector)

                val isPreviewStab = cameraInfo?.let {
                    Preview.getPreviewCapabilities(it).isStabilizationSupported
                } ?: false //если стаба нет

                val targetFPS = cameraInfo?.let { info ->
                    info.supportedFrameRateRanges.find { range ->
                        range.upper == 60 && range.lower == 60
                    } ?: cameraInfo.supportedFrameRateRanges.find { it.upper == 60 }
                }

                val resolutionSelector = ResolutionSelector.Builder()
                    .setResolutionFilter {  supportedSizes, _ ->
                        supportedSizes.filter { it.width == 1920 || it.height == 1080 }
                    }.build()

                val previewBuilder = Preview.Builder()
                    .setResolutionSelector(resolutionSelector)

                val camera2Extender = Camera2Interop.Extender(previewBuilder)
                camera2Extender.setCaptureRequestOption(
                    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
                    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON
                )
                camera2Extender.setCaptureRequestOption(
                    CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE,
                    CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON,
                )
                camera2Extender.setCaptureRequestOption(
                    CaptureRequest.DISTORTION_CORRECTION_MODE,
                    CaptureRequest.DISTORTION_CORRECTION_MODE_HIGH_QUALITY
                )
                camera2Extender.setCaptureRequestOption(
                    CaptureRequest.EDGE_MODE,
                    CaptureRequest.EDGE_MODE_HIGH_QUALITY
                )
                camera2Extender.setCaptureRequestOption(
                    CaptureRequest.NOISE_REDUCTION_MODE,
                    CaptureRequest.NOISE_REDUCTION_MODE_HIGH_QUALITY
                )


                if(targetFPS != null) {
                    previewBuilder.setTargetFrameRate(targetFPS)
                }

                if(isPreviewStab) {
                    previewBuilder.setPreviewStabilizationEnabled(true)
                }

                previewUseCase = previewBuilder
                    .build()
                    .also {
                        it.surfaceProvider = surfaceProvider
                    }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val poseAnalysis = poseAnalysis()

                cameraProvider?.unbindAll()

                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    previewUseCase,
                    poseAnalysis,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка камеры ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraProvider?.unbindAll()
    }

    private fun poseAnalysis(): ImageAnalysis { //подготовка для передачи
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    analyze(imageProxy)
                }
            }
    }


    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalGetImage::class)
    private fun analyze(imageProxy: ImageProxy) { //сам анализ
        if(!_active.value) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: return imageProxy.close()
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)


        if (_faceCamera.value) {
            analyzeFace(image, imageProxy)
        } else
            analyzePose(image, imageProxy)

    }

    fun analyzePose(image: InputImage, imageProxy: ImageProxy) {
        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                if(!_active.value) return@addOnSuccessListener

                _detectedPose.value = pose

                poseClassifier?.let { classifier ->
                    val classification = classifier.classify(pose)
                    val resultBuilder = StringBuilder()

                    for(counter in repetitionCounter) {
                        val reps = counter.addClassificationResult(classification)
                        val displayName = counter.className
                            .replace("pushups_down", "push-ups")
                            .replace("_down", "")
                            .uppercase()

                        if(classification.getClassConfidence(counter.className) > 3f) {
                            _classificationResult.value = "$displayName: $reps"
                        }

                        resultBuilder.append("$displayName: $reps\n")
                    }

                    _classificationEnd.value = resultBuilder.toString().trim()
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun analyzeFace(image: InputImage, imageProxy: ImageProxy) {
        faceDetector.process(image)
            .addOnSuccessListener { face ->
                _detectedFace.value = face
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun switchAnalyze() {
        _faceCamera.value = !_faceCamera.value
    }

    fun loadPoseClassifier() {
        viewModelScope.launch(Dispatchers.IO) {
            val poseSample = mutableListOf<PoseSample>()
            try {
                context.applicationContext.assets.open("fitness_pose_samples.csv").bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        if(line.isNotBlank()) {
                            val sample = PoseSample.getPoseSample(line, ",")
                            if(sample != null) poseSample.add(sample)
                        }
                    }
                }
                poseClassifier = PoseClassifier(poseSample)

                repetitionCounter.clear()
                repetitionCounter.add(RepetitionCounter("pushups_down"))
                repetitionCounter.add(RepetitionCounter("squats_down"))
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка загрузки сэмплов ${e.message}")
            }
        }
    }

    fun switch() {
        _frontCamera.value = !_frontCamera.value
    }

    fun start() {
        _active.value = true
    }

    fun stop() {
        _active.value = false
        _detectedPose.value = null
        _classificationResult.value = ""
    }
}