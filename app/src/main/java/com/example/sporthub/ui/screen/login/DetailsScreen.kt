package com.example.sporthub.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sporthub.ui.components.BottomBarContinue
import com.example.sporthub.ui.theme.LightBlue
import com.example.sporthub.ui.theme.OffWhite
import com.example.sporthub.ui.theme.black
import com.example.sporthub.ui.theme.gray
import com.example.sporthub.ui.viewmodel.AuthState
import com.example.sporthub.ui.viewmodel.LoginViewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    nameState: String,
    onNameChange: (String) -> Unit,
    genderState: String,
    onGenderChange: (String) -> Unit,
    weightState: String,
    heightState: String,
    birthdateState: String,
    onWeight: () -> Unit,
    onHeight: () -> Unit,
    openDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
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
                keyboardController?.hide()
            }
            .padding(bottom = 12.dp, top = 70.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
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
            value = nameState,
            onValueChange = onNameChange,
            singleLine = true,
            placeholder = {
                Text(
                    "Name",
                    style = MaterialTheme.typography.titleLarge.copy(color = gray),
                    fontSize = 24.sp
                )
            },
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
            value = genderState,
            onValueChange = onGenderChange,
            singleLine = true,
            placeholder = {
                Text(
                    "Gender",
                    style = MaterialTheme.typography.titleLarge.copy(color = gray)
                )
            },
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
            value = weightState,
            onValueChange = { },
            readOnly = true,
            enabled = false,
            placeholder = {
                Text(
                    "Weight",
                    style = MaterialTheme.typography.titleLarge.copy(color = gray)
                )
            },
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
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = gray,
                disabledPlaceholderColor = Color.Transparent,
                disabledLeadingIconColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onWeight()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
        )

        TextField(
            value = heightState,
            onValueChange = {  },
            readOnly = true,
            enabled = false,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    "Height",
                    style = MaterialTheme.typography.titleLarge.copy(color = gray)
                )
            },
            visualTransformation = { height ->
                val suffix = if (height.text.isNotEmpty()) "cm" else ""
                val out = height.text + suffix
                val offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = offset
                    override fun transformedToOriginal(offset: Int): Int =
                        offset.coerceAtMost(height.length)
                }
                TransformedText(
                    AnnotatedString(out),
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
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = gray,
                disabledPlaceholderColor = Color.Transparent,
                disabledLeadingIconColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onHeight()
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
        )
        TextField(
            value = birthdateState,
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            placeholder = {
                Text(
                    "Birthdate",
                    style = MaterialTheme.typography.titleLarge.copy(color = gray)
                )
            },
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
                    openDialog()
                },
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                color = gray
            ),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = gray,
                disabledPlaceholderColor = Color.Transparent,
                disabledLeadingIconColor = Color.Transparent
            ),
            enabled = false
        )

        Spacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(120.dp)
        )
    }
}

@Composable
fun DetailsBottomBar(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    weightState: String,
    heightState: String,
    onWeight: () -> Unit,
    onHeight: () -> Unit,
    openDialog: () -> Unit,
    datePickerState: DatePickerState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    birthdateState: String,
    modifier: Modifier = Modifier
) {
    val userData by loginViewModel.currentUser.collectAsState()

    var nameState by remember(userData) { mutableStateOf(userData?.name ?: "") }
    var genderState by remember(userData) { mutableStateOf(userData?.gender ?: "") }

    val isFormValid = nameState.isNotBlank() &&
            genderState.isNotBlank() &&
            weightState.isNotBlank() &&
            heightState.isNotBlank() &&
            birthdateState.isNotEmpty()

    val authState by loginViewModel.authState.collectAsState()
    val isLoading = authState is AuthState.Loading

    Box(modifier = modifier.fillMaxSize()) {
        val backdrop = rememberLayerBackdrop {
            drawContent()
        }

        DetailsScreen(
            nameState = nameState,
            onNameChange = { nameState = it },
            genderState = genderState,
            onGenderChange = { genderState = it },
            weightState = weightState,
            heightState = heightState,
            birthdateState = birthdateState,
            modifier = Modifier.layerBackdrop(backdrop),
            onWeight = onWeight,
            onHeight = onHeight,
            openDialog = openDialog,
        )

        BottomBarContinue(
            backdrop,
            navController = navController,
            onClick = {
                if (isFormValid) {
                    val weight = weightState.toIntOrNull() ?: 0
                    val height = heightState.toIntOrNull() ?: 0
                    val birthdate = datePickerState.selectedDateMillis ?: userData?.birthdate ?: 0L

                    loginViewModel.detailsUser(
                        nameState, genderState, weight, height, birthdate
                    )
                    navController.navigate("start_screen")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            isLoading = isLoading
        )
    }
}
