package com.example.sporthub.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.sporthub.data.repository.SportHubRepository
import com.example.sporthub.data.sporthub.FaceEntity
import com.example.sporthub.data.sporthub.SportHubDatabase
import com.example.sporthub.llm.Gemini
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FaceViewModel(application: Application) : AndroidViewModel(application) {
    private val db = SportHubDatabase.getInstance(application)
    private val repository = SportHubRepository(db.sportHubDao, db.healthDao, db.workoutDao, db.faceDao)

    val faceData: LiveData<FaceEntity?> = repository.getFaceData.asLiveData()

    private val _sensitive = MutableStateFlow(0f)
    val sensitive = _sensitive.asStateFlow()

    private val _loadBitmap = MutableStateFlow<Bitmap?>(null)


    private val _acne = MutableStateFlow(0)

    private val _dryness = MutableStateFlow(0)

    private val _moisture = MutableStateFlow(0)

    private val _scan = MutableStateFlow(false)
    val scan = _scan.asStateFlow()


    fun addSensitive(value: Float) {
        viewModelScope.launch {
            repository.addFace(
                sensitive = value,
                acne = faceData.value?.acne ?: 0,
                dryness = faceData.value?.dryness ?: 0,
                moisture = faceData.value?.moisture ?: 0,
            )
            Log.d("MyLog", "Данные чувтсвительтности лица сохранено")
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun processCaptureImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _loadBitmap.value = null
            try {
                val faceEntity = FaceEntity(
                    id = 1,
                    sensitive = faceData.value?.sensitive ?: 0f,
                    acne = faceData.value?.acne ?: 0,
                    dryness = faceData.value?.dryness ?: 0,
                    moisture = faceData.value?.moisture ?: 0,
                )
                val result = Gemini.analyzeFace(bitmap, faceEntity)

                result?.let {
                    if(it.size >= 3)
                        _acne.value = it[0]
                        _dryness.value = it[1]
                        _moisture.value = it[2]

                        repository.addFace(
                            faceEntity.sensitive,
                            _acne.value,
                            _dryness.value,
                            _moisture.value,
                        )
                }
                Log.d("MyLog", "Изображение загружено")
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка загрузки изображения", e)
            } finally {
                stopScan()
            }
        }
    }

    fun startScan() {
        _scan.value = true
    }

    fun stopScan() {
        _scan.value = false
    }

    fun selectWidget() {
        viewModelScope.launch {
            repository.updateWidget(widget = !(faceData.value?.widget ?: false))
        }
    }
}