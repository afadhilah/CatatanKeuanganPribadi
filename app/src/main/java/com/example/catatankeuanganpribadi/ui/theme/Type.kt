package com.example.catatankeuanganpribadi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AppSerif = FontFamily.Serif

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = AppSerif),
    displayMedium = TextStyle(fontFamily = AppSerif),
    displaySmall = TextStyle(fontFamily = AppSerif),
    headlineLarge = TextStyle(
        fontFamily = AppSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(fontFamily = AppSerif),
    headlineSmall = TextStyle(fontFamily = AppSerif),
    titleLarge = TextStyle(
        fontFamily = AppSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(fontFamily = AppSerif),
    titleSmall = TextStyle(fontFamily = AppSerif),
    bodyLarge = TextStyle(fontFamily = AppSerif),
    bodyMedium = TextStyle(fontFamily = AppSerif),
    bodySmall = TextStyle(fontFamily = AppSerif),
    labelLarge = TextStyle(fontFamily = AppSerif),
    labelMedium = TextStyle(fontFamily = AppSerif),
    labelSmall = TextStyle(
        fontFamily = AppSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp, // Dikecilkan sedikit untuk menghindari teks terpotong
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)
