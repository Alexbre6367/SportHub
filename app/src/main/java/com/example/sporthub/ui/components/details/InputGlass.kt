package com.example.sporthub.ui.components.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.baseGlass
import com.example.sporthub.ui.screen.login.DetailsBottomBar
import com.example.sporthub.ui.theme.LightGray
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerState
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun InputGlass(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val userData by loginViewModel.currentUser.collectAsState()

    var weightState by remember(userData) {
        mutableStateOf(
            if ((userData?.weight ?: 0) > 0) userData?.weight.toString() else ""
        )
    }
    var heightState by remember(userData) {
        mutableStateOf(
            if ((userData?.height ?: 0) > 0) userData?.height.toString() else ""
        )
    }

    var birthdateState by remember(userData) {
        mutableStateOf(if ((userData?.birthdate ?: 0L) != 0L) {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(userData!!.birthdate))
        } else "")
    }

    var weight by remember { mutableStateOf(false) }
    var height by remember { mutableStateOf(false) }
    var age by remember { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = userData?.birthdate?.takeIf { it != 0L }
    )

    val startWeight = userData?.weight ?: 70
    val startHeight = userData?.height ?: 180

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = if((userData?.birthdate ?: 0L) != 0L) userData!!.birthdate else 946684800000L
    }

    val startDay = calendar.get(Calendar.DAY_OF_MONTH)
    val startMonth = calendar.get(Calendar.MONTH) + 1
    val startYear = calendar.get(Calendar.YEAR)

    val weightIndex = (startWeight - 30).coerceIn(0, 140)
    val heightIndex = (startHeight - 100).coerceIn(0, 210)

    val dayIndex = (startDay - 1).coerceIn(0, 31)
    val monthIndex = (startMonth - 1).coerceIn(0, 12)
    val yearIndex = (startYear - 1927).coerceIn(0, 83)

    val weightPickerState = rememberFWheelPickerState(initialIndex = weightIndex)
    val heightPickerState = rememberFWheelPickerState(initialIndex = heightIndex)

    val dayPickerState = rememberFWheelPickerState(initialIndex = dayIndex)
    val monthPickerState = rememberFWheelPickerState(initialIndex = monthIndex)
    val yearPickerState = rememberFWheelPickerState(initialIndex = yearIndex)



    BackHandler(enabled = true) {
        if(weight) {
            weight = false
        }
        else if(height) {
            height = false
        }
        else if(age) {
            age = false
        }
        else {
            navController.popBackStack()
        }
    }

    Box(Modifier.fillMaxSize()) {

        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        DetailsBottomBar(
            navController,
            loginViewModel,
            weightState = weightState,
            heightState = heightState,
            onWeight = { weight = true },
            onHeight = { height = true },
            datePickerState = datePickerState,
            birthdateState = birthdateState,
            openDialog = { age = true },
            onWeightChange = { weightState = it },
            onHeightChange = { heightState = it },
            onAgeChange = { birthdateState = it },
            modifier = Modifier.layerBackdrop(backdrop)
        )

        if (weight) {
            WeightInput(
                pickerState = weightPickerState,
                onWeightChange = { selection ->
                    weightState = selection
                },
                onDismiss = { weight = false },
                backdrop = backdrop
            )
        }

        if (height) {
            HeightInput(
                pickerState = heightPickerState,
                onHeightChange = { selection ->
                    heightState = selection
                },
                onDismiss = { height = false },
                backdrop = backdrop
            )
        }

        if(age) {
            AgeInput(
                pickerStateDay = dayPickerState,
                pickerStateMonth = monthPickerState,
                pickerStateYear = yearPickerState,
                datePickerState = datePickerState,
                onAgeChange = { selection ->
                    birthdateState = selection
                },
                onDismiss = { age = false },
                backdrop = backdrop
            )
        }
    }
}

@Composable
fun WeightInput(
    pickerState: FWheelPickerState,
    onWeightChange: (String) -> Unit,
    onDismiss: () -> Unit,
    backdrop: Backdrop
) {
    BoxGlass(
        backdrop,
        onClick = {
            onWeightChange((pickerState.currentIndex + 30).toString())
            onDismiss()
        },
        onDismiss,
        textCard = "Log weight",
        textCaps = "MINIMUM ALLOWED WEIGHT IS 30KG",
        content = {
            FVerticalWheelPicker(
                count = 101,
                state = pickerState,
                modifier = Modifier.fillMaxWidth(),
                itemHeight = 24.dp,
                unfocusedCount = 3
            ) { index ->
                val currentWeight = index + 30
                val isFocused = index == pickerState.currentIndex
                Text(
                    text = "$currentWeight kg",
                    color = if (isFocused) black else gray.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = if (isFocused) 20.sp else 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun HeightInput(
    pickerState: FWheelPickerState,
    onHeightChange: (String) -> Unit,
    onDismiss: () -> Unit,
    backdrop: Backdrop
) {
    BoxGlass(
        backdrop,
        onClick = {
            onHeightChange((pickerState.currentIndex + 100).toString())
            onDismiss()
        },
        onDismiss,
        textCard = "Log height",
        textCaps = "MINIMUM ALLOWED HEIGHT IS 100CM",
        content = {
            FVerticalWheelPicker(
                count = 121,
                state = pickerState,
                modifier = Modifier.fillMaxWidth(),
                itemHeight = 24.dp,
                unfocusedCount = 3
            ) { index ->
                val currentHeight = index + 100
                val isFocused = index == pickerState.currentIndex
                Text(
                    text = "$currentHeight cm",
                    color = if (isFocused) black else gray.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = if (isFocused) 20.sp else 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun AgeInput(
    pickerStateDay: FWheelPickerState,
    pickerStateMonth: FWheelPickerState,
    pickerStateYear: FWheelPickerState,
    datePickerState: DatePickerState,
    onAgeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    backdrop: Backdrop
) {
    val daysInMonth = remember(pickerStateMonth.currentIndex, pickerStateYear.currentIndex) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, pickerStateYear.currentIndex + 1927)
        cal.set(Calendar.MONTH, pickerStateMonth.currentIndex)
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    LaunchedEffect(daysInMonth) {
        if(pickerStateDay.currentIndex >= daysInMonth) {
            pickerStateDay.scrollToIndex(daysInMonth - 1)
        }
    }

    BoxGlass(
        backdrop,
        onClick = {
            val day = pickerStateDay.currentIndex + 1
            val month = pickerStateMonth.currentIndex
            val year = pickerStateYear.currentIndex + 1927

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            datePickerState.selectedDateMillis = calendar.timeInMillis

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            onAgeChange(sdf.format(calendar.time))
            onDismiss()
        },
        onDismiss,
        textCard = "Log age",
        textCaps = "MINIMUM ALLOWED YEAR IS 2010",
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FVerticalWheelPicker(
                    count = daysInMonth,
                    state = pickerStateDay,
                    modifier = Modifier.weight(1f),
                    itemHeight = 24.dp,
                    unfocusedCount = 3
                ) { index ->
                    val currentDay = index + 1
                    val isFocused = index == pickerStateDay.currentIndex
                    Text(
                        text = "$currentDay",
                        color = if (isFocused) black else gray.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = if (isFocused) 20.sp else 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                FVerticalWheelPicker(
                    count = 12,
                    state = pickerStateMonth,
                    modifier = Modifier.weight(1f),
                    itemHeight = 24.dp,
                    unfocusedCount = 3
                ) { index ->
                    val currentMonth = index + 1
                    val isFocused = index == pickerStateMonth.currentIndex
                    Text(
                        text = "$currentMonth",
                        color = if (isFocused) black else gray.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = if (isFocused) 20.sp else 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                FVerticalWheelPicker(
                    count = 84,
                    state = pickerStateYear,
                    modifier = Modifier.weight(1f),
                    itemHeight = 24.dp,
                    unfocusedCount = 3
                ) { index ->
                    val currentYear = index + 1927
                    val isFocused = index == pickerStateYear.currentIndex
                    Text(
                        text = "$currentYear",
                        color = if (isFocused) black else gray.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = if (isFocused) 20.sp else 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun BoxGlass(
    backdrop: Backdrop,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    textCard: String,
    textCaps: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .baseGlass(backdrop, blur = 8.dp, shape = RoundedCornerShape(0.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .baseGlass(backdrop, blur = 30.dp, shape = RoundedCornerShape(36.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = textCard,
                color = black,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .baseGlass(backdrop, blur = 8.dp, shape = RoundedCornerShape(24.dp)),
                )

                content()
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = textCaps,
                color = LightGray,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = onClick,
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
                    text = "Continue",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
            }
        }
    }
}