package com.example.sporthub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.sporthub.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val Fredoka = GoogleFont("Fredoka")
val NunitoFontFamily = FontFamily(
    Font(googleFont = Fredoka, fontProvider = provider),
)


val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontSize = 24.sp
    ),
)