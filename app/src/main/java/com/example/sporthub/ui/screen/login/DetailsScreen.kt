package com.example.sporthub.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.LoginViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val nameState = remember { mutableStateOf("") }
    val genderState = remember { mutableStateOf("") }
    val weightState = remember { mutableStateOf("") }
    val heightState = remember { mutableStateOf("") }
    val birthdateState = remember { mutableStateOf("") }
    val isFormValid = nameState.value.isNotBlank() &&
            genderState.value.isNotBlank() &&
            weightState.value.isNotBlank() &&
            heightState.value.isNotBlank() &&
            birthdateState.value.isNotBlank()

    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LightBlue, OffWhite), startY = 0f, endY = 1500f
                )
            )
            .verticalScroll(scrollState)
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                focusManager.clearFocus()
            }
            .padding(bottom = 24.dp, top = 70.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp),
    ) {
        Text(
            text = "Enter your details",
            color = black,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "This will help us personalize your \nexperience and goals",
            color = gray,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )
        Spacer(Modifier.height(50.dp))
        TextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            singleLine = true,
            placeholder = { Text("Name", style = MaterialTheme.typography.titleLarge.copy(color = gray), fontSize = 24.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = gray
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = genderState.value,
            onValueChange = { genderState.value = it },
            singleLine = true,
            placeholder = { Text("Gender", style = MaterialTheme.typography.titleLarge.copy(color = gray)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Wc,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = gray
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = weightState.value,
            onValueChange = { newWeight ->
                if (newWeight.all { it.isDigit() }) {
                    weightState.value = newWeight
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("Weight", style = MaterialTheme.typography.titleLarge.copy(color = gray)) },
            visualTransformation = { weight ->
                val suffix = if (weight.text.isNotEmpty()) "kg" else ""
                val out = weight.text + suffix
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = offset
                    override fun transformedToOriginal(offset: Int): Int =
                        offset.coerceAtMost(weight.length)
                }
                TransformedText(
                    AnnotatedString(out),
                    offsetMapping
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Scale,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = gray
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = heightState.value,
            onValueChange = { newHeight ->
                if (newHeight.all { it.isDigit() }) {
                    heightState.value = newHeight
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("Height", style = MaterialTheme.typography.titleLarge.copy(color = gray)) },
            visualTransformation = { height ->
                val suffix = if (height.text.isNotEmpty()) "cm" else ""
                val out = height.text + suffix
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = offset
                    override fun transformedToOriginal(offset: Int): Int =
                        offset.coerceAtMost(height.length)
                }
                TransformedText(
                    androidx.compose.ui.text.AnnotatedString(out),
                    offsetMapping
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Height,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = gray
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = birthdateState.value,
            onValueChange = {  },
            readOnly = true,
            singleLine = true,
            placeholder = { Text("Birthdate", style = MaterialTheme.typography.titleLarge.copy(color = gray)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Cake,
                    contentDescription = null,
                    tint = gray,
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusManager.clearFocus()
                    openDialog.value = true
                },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            enabled = false
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .clickable(
                        onClick = {
                            navController.popBackStack()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .background(
                        color = Color.White, shape = CircleShape
                    )
                    .border(
                        width = 1.dp, color = Color.White, shape = CircleShape
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

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
                    .padding(start = 12.dp)
                    .clickable(
                        onClick = {
                            if (isFormValid) {
                                val weight = weightState.value.toFloatOrNull() ?: 0f

                                val height = heightState.value.toIntOrNull() ?: 0

                                val birthdate = datePickerState.selectedDateMillis ?: 0L

                                loginViewModel.detailsUser(
                                    nameState.value, genderState.value, weight, height, birthdate
                                )
                                navController.navigate("start_screen")
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                    .background(
                        color = if (isFormValid) black else black.copy(alpha = 0.5f),
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

            if (openDialog.value) {
                DatePickerDialog(
                    onDismissRequest = { openDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDate = datePickerState.selectedDateMillis
                            if (selectedDate != null) {
                                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
                                birthdateState.value = sdf.format(Date(selectedDate))
                            }
                            openDialog.value = false
                        }) {
                            Text("OK", color = black, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDialog.value = false }) {
                            Text("Cancel", color = black, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = DatePickerDefaults.colors(
                        containerColor = OffWhite,
                    )
                ) {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            containerColor = OffWhite,
                            titleContentColor = gray,
                            headlineContentColor = black,
                            weekdayContentColor = gray,
                            dayContentColor = black,
                            selectedDayContainerColor = black,
                            selectedDayContentColor = Color.White,
                            todayContentColor = black,
                            todayDateBorderColor = black
                        )
                    )
                }
            }
        }
    }
}



