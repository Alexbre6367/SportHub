package com.example.sporthub.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sporthub.data.sporthub.ExerciseEntity
import com.example.sporthub.data.sporthub.WorkoutEntity
import com.example.sporthub.data.sporthub.WorkoutWithExercises
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.colorError
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.WorkoutViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlinx.coroutines.delay

@Composable
fun AddWorkoutScreen(
    workoutViewModel: WorkoutViewModel,
    exercises: SnapshotStateList<ExerciseEntity>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val selectedId by workoutViewModel.selectedExercises.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    DisposableEffect(Unit) {
        onDispose {
            workoutViewModel.clearSelectedExercises()
        }
    }

    LaunchedEffect(selectedId) {
        if(selectedId.isNotEmpty()) {
            delay(5000)
            workoutViewModel.clearSelectedExercises()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .clickable (
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .statusBarsPadding()
    ) {
        Spacer(Modifier.height(86.dp))
        exercises.forEachIndexed { index, exercise ->
            Exercises(
                exerciseEdit = exercise,
                onUpdate = { update ->
                    exercises[index] = update
                },
                isSelected = selectedId.contains(exercise.exerciseId),
                onClick = { if(selectedId.contains(exercise.exerciseId)) workoutViewModel.toggleExercises(exercise.exerciseId) },
                onLongClick = {
                    workoutViewModel.toggleExercises(exercise.exerciseId)
                }
            )
            Spacer(Modifier.height(20.dp))
        }

        AddExerciseButton(
            onClick = {
                val newId = (exercises.minOfOrNull { it.exerciseId } ?: 0).coerceAtMost(0) - 1
                exercises.add(
                    ExerciseEntity(
                        exerciseId = newId,
                        workoutOwnerId = 0
                    )
                )
            }
        )
        Spacer(Modifier.height(500.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddWorkoutBar(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    workoutEdit: WorkoutWithExercises? = null
) {
    var nameWorkout by remember { mutableStateOf(workoutEdit?.workout?.name.orEmpty()) }
    val exercises = remember {
        mutableStateListOf<ExerciseEntity>().apply {
            if(workoutEdit != null && workoutEdit.exercises.isNotEmpty()) {
                addAll(workoutEdit.exercises)
            } else {
                add(ExerciseEntity(workoutOwnerId = 0, exerciseId = -1))
            }
        }
    }

    var isDelete by remember { mutableStateOf(false) }

    val selectedId by workoutViewModel.selectedExercises.collectAsState()
    val exercisesSelected = selectedId.isNotEmpty()

    LaunchedEffect(isDelete) {
        if(isDelete) {
            delay(5000)
            isDelete = false
        }
    }

    var isKeyboard by remember { mutableStateOf(false) }
    val isVisibleKeyboard = WindowInsets.isImeVisible

    LaunchedEffect(isVisibleKeyboard) {
        isKeyboard = isVisibleKeyboard
    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        AddWorkoutScreen(
            workoutViewModel,
            exercises = exercises,
            modifier = Modifier.layerBackdrop(backdrop)
        )

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp)
                .height(58.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = nameWorkout,
                    onValueChange = { nameWorkout = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 26.sp,
                        color = gray
                    ),
                    cursorBrush = SolidColor(gray),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (nameWorkout.isEmpty()) {
                                Text(
                                    "Name",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 26.sp,
                                    color = gray
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = {
                            drawRect(OffWhite, alpha = 0.4f, blendMode = BlendMode.Overlay)
                            drawRect(OffWhite.copy(alpha = 0.5f))
                        }
                    )
                    .size(58.dp)
                    .clickable(
                        onClick = {
                            if(exercisesSelected) {
                                exercises.removeAll { it.exerciseId in selectedId}
                                workoutViewModel.deleteSelectedExercises()
                                if(exercises.isEmpty()) {
                                    exercises.add(ExerciseEntity(workoutOwnerId = 0, exerciseId = -1))
                                }
                            } else if (workoutEdit != null) {
                                if (isDelete) {
                                    workoutViewModel.deleteWorkoutWithExercise(workoutEdit.workout)
                                    keyboardController?.hide()
                                    navController.popBackStack()
                                } else {
                                    isDelete = true
                                }
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when {
                        exercisesSelected -> Icons.Default.Delete
                        isDelete -> Icons.Default.Check
                        else -> Icons.Default.DeleteOutline
                    },
                    contentDescription = null,
                    tint = when {
                        exercisesSelected -> colorError
                        isDelete -> colorError
                        else -> gray
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(58.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { CircleShape },
                        effects = {
                            vibrancy()
                            blur(2f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        }
                    )
                    .size(58.dp)
                    .clickable(
                        onClick = {
                            if (isKeyboard) {
                                keyboardController?.hide()
                            } else {
                                navController.popBackStack()
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .height(58.dp)
                    .weight(1f)
                    .clickable(
                        onClick = {
                            if (nameWorkout.isNotBlank() && exercises.isNotEmpty() && exercises.all { it.body.isNotBlank() }) {
                                workoutViewModel.addWorkoutWithExercise(
                                    WorkoutEntity(
                                        workoutId = workoutEdit?.workout?.workoutId ?: 0,
                                        name = nameWorkout
                                    ),
                                    exercises,
                                )
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Fill in all fields",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .background(
                        color = black,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun Exercises(
    exerciseEdit: ExerciseEntity,
    onUpdate: (ExerciseEntity) -> Unit,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val exercise = exerciseEdit.body
    val repetitions = if (exerciseEdit.repetitions == 0) "" else exerciseEdit.repetitions.toString()
    val sets = if (exerciseEdit.sets == 0) "" else exerciseEdit.sets.toString()
    val kg = exerciseEdit.kg

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                if (isSelected) 1.dp else 0.dp,
                if (isSelected) black else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .drawBackdrop(
                    backdrop = rememberLayerBackdrop(),
                    shape = { RoundedCornerShape(18.dp) },
                    effects = { }
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = exerciseEdit.body,
                onValueChange = {
                    onUpdate(exerciseEdit.copy(body = it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    color = gray
                ),
                cursorBrush = SolidColor(gray),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (exercise.isEmpty()) {
                            Text(
                                "Exercise",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                color = gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .height(60.dp)
                    .width(100.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reps",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    color = gray
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { RoundedCornerShape(18.dp) },
                            effects = { }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = if (exerciseEdit.repetitions == 0) "" else exerciseEdit.repetitions.toString(),
                        onValueChange = { newValue ->
                            if(newValue.length <= 3 && newValue.all { it.isDigit() }) {
                                onUpdate(exerciseEdit.copy(repetitions = newValue.toIntOrNull() ?: 0))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            color = gray,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(gray),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (repetitions.isEmpty()) {
                                    Text(
                                        "0",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 20.sp,
                                        color = gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .height(60.dp)
                    .width(100.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sets",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    color = gray
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { RoundedCornerShape(18.dp) },
                            effects = { }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = if (exerciseEdit.sets == 0) "" else exerciseEdit.sets.toString(),
                        onValueChange = { newValue ->
                            if(newValue.length <= 3 && newValue.all { it.isDigit() }) {
                                onUpdate(exerciseEdit.copy(sets = newValue.toIntOrNull() ?: 0))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            color = gray,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(gray),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (sets.isEmpty()) {
                                    Text(
                                        "0",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 20.sp,
                                        color = gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .height(60.dp)
                    .width(100.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kg",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    color = gray
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .drawBackdrop(
                            backdrop = rememberLayerBackdrop(),
                            shape = { RoundedCornerShape(18.dp) },
                            effects = { }
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = exerciseEdit.kg,
                        onValueChange = { newValue ->
                            if(newValue.length <= 5) onUpdate(exerciseEdit.copy(kg = newValue))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            color = gray,
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(gray),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (kg.isEmpty()) {
                                    Text(
                                        "0",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 20.sp,
                                        color = gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddExerciseButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = gray,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Add a new exercise",
            style = MaterialTheme.typography.titleLarge,
            color = gray,
            fontSize = 20.sp
        )
    }
}