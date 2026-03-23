package com.smartbit.mobile.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Example composable showing how to use animated glowing effects
 * You can use these patterns in your screens
 */

/**
 * Animated glowing card with shimmer effect
 */
@Composable
fun GlowingCard(
    pathwayTheme: PathwayTheme,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .animatedGlowingBackground(pathwayTheme, cornerRadius = 16f)
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        androidx.compose.foundation.layout.Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Pulsing glowing button-like container
 */
@Composable
fun PulsingGlowBox(
    pathwayTheme: PathwayTheme,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .pulsingGlow(pathwayTheme, intensity = 1f)
            .background(
                when (pathwayTheme) {
                    PathwayTheme.GYM -> GymGradientStart.copy(alpha = 0.8f)
                    PathwayTheme.DIET -> DietGradientStart.copy(alpha = 0.8f)
                    PathwayTheme.WELLNESS -> WellnessGradientStart.copy(alpha = 0.8f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Animated gradient shift container
 */
@Composable
fun GradientShiftBox(
    pathwayTheme: PathwayTheme,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .animatedGradientShift(pathwayTheme, duration = 3000)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

