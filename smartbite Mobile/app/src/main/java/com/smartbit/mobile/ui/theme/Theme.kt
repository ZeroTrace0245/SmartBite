package com.smartbit.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SmartBitTheme(
    darkTheme: Boolean = true, // Force dark theme for modern redesigned navy look
    pathwayTheme: PathwayTheme = PathwayTheme.WELLNESS,
    content: @Composable () -> Unit
) {
    // Redesigned Navy Theme (Clean and Neat)
    val colorScheme = when (pathwayTheme) {
        PathwayTheme.GYM -> {
            darkColorScheme(
                primary = GymGradientStart,
                secondary = GymGradientEnd,
                tertiary = BrightYellow,
                surface = Color(0xFF1D2136),
                background = Color(0xFF0A0E21),
                onSurface = Color.White,
                onBackground = Color.White,
                outline = GymGradientStart.copy(alpha = 0.5f)
            )
        }

        PathwayTheme.DIET -> {
            darkColorScheme(
                primary = DietGradientStart,
                secondary = DietGradientEnd,
                tertiary = BrightYellow,
                surface = Color(0xFF1D2136),
                background = Color(0xFF0A0E21),
                onSurface = Color.White,
                onBackground = Color.White,
                outline = DietGradientStart.copy(alpha = 0.5f)
            )
        }

        PathwayTheme.WELLNESS -> {
            darkColorScheme(
                primary = WellnessGradientStart,
                secondary = WellnessGradientEnd,
                tertiary = Cyan,
                surface = Color(0xFF1D2136),
                background = Color(0xFF0A0E21),
                onSurface = Color.White,
                onBackground = Color.White,
                outline = WellnessGradientStart.copy(alpha = 0.5f)
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
