package com.example.catatankeuanganpribadi.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PurpleAccent,
    onPrimary = Color.White,
    primaryContainer = PurpleAccentDark,
    onPrimaryContainer = Color.White,

    secondary = TealBlue,
    onSecondary = DeepNavy,
    secondaryContainer = ElevatedSurface,
    onSecondaryContainer = TextPrimary,

    tertiary = MintGreen,
    onTertiary = DeepNavy,
    tertiaryContainer = MintGreenBg,
    onTertiaryContainer = MintGreen,

    error = CoralRed,
    onError = Color.White,
    errorContainer = CoralRedBg,
    onErrorContainer = CoralRed,

    background = DeepNavy,
    onBackground = TextPrimary,

    surface = MutedNavy,
    onSurface = TextPrimary,
    surfaceVariant = ElevatedSurface,
    onSurfaceVariant = TextSecondary,

    outline = CardBorder,
    outlineVariant = CardBorder,
    inverseSurface = OffWhite,
    inverseOnSurface = DeepNavy
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleAccent,
    onPrimary = Color.White,
    primaryContainer = PurpleAccentLight,
    onPrimaryContainer = Color.White,

    secondary = TealBlue,
    onSecondary = Color.White,
    secondaryContainer = LightCard,
    onSecondaryContainer = TextPrimaryLight,

    tertiary = MintGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0FFF5),
    onTertiaryContainer = Color(0xFF006B50),

    error = CoralRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFE5E5),
    onErrorContainer = CoralRed,

    background = OffWhite,
    onBackground = TextPrimaryLight,

    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondaryLight,

    outline = LightBorder,
    outlineVariant = LightBorder,
    inverseSurface = DeepNavy,
    inverseOnSurface = TextPrimary
)

@Composable
fun CatatanKeuanganPribadiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
