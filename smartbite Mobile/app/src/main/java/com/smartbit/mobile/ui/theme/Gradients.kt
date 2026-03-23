package com.smartbit.mobile.ui.theme

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

/**
 * Creates an animated glowing gradient brush with shimmer effect
 */
@Composable
fun getAnimatedGlowingGradient(pathwayTheme: PathwayTheme): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "glowTransition")
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPosition"
    )

    return when (pathwayTheme) {
        PathwayTheme.GYM -> {
            Brush.linearGradient(
                colors = listOf(
                    GymGradientStart.copy(alpha = 0.8f),
                    GymGradientStart,
                    GymGradientEnd,
                    GymGradientEnd.copy(alpha = 0.8f)
                ),
                start = Offset(shimmerPosition * 1000f, 0f),
                end = Offset(shimmerPosition * 1000f + 500f, 1000f)
            )
        }
        PathwayTheme.DIET -> {
            Brush.linearGradient(
                colors = listOf(
                    DietGradientStart.copy(alpha = 0.8f),
                    DietGradientStart,
                    DietGradientEnd,
                    DietGradientEnd.copy(alpha = 0.8f)
                ),
                start = Offset(shimmerPosition * 1000f, 0f),
                end = Offset(shimmerPosition * 1000f + 500f, 1000f)
            )
        }
        PathwayTheme.WELLNESS -> {
            Brush.linearGradient(
                colors = listOf(
                    WellnessGradientStart.copy(alpha = 0.8f),
                    WellnessGradientStart,
                    WellnessGradientEnd,
                    WellnessGradientEnd.copy(alpha = 0.8f)
                ),
                start = Offset(shimmerPosition * 1000f, 0f),
                end = Offset(shimmerPosition * 1000f + 500f, 1000f)
            )
        }
    }
}

/**
 * Modifier to apply animated glowing effect with shadow and border
 */
@Composable
fun Modifier.animatedGlowingBackground(
    pathwayTheme: PathwayTheme,
    cornerRadius: Float = 12f
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "glowEffect")
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val glowBlur by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 16f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowBlur"
    )

    val (primaryColor, secondaryColor) = when (pathwayTheme) {
        PathwayTheme.GYM -> GymGradientStart to GymGradientEnd
        PathwayTheme.DIET -> DietGradientStart to DietGradientEnd
        PathwayTheme.WELLNESS -> WellnessGradientStart to WellnessGradientEnd
    }

    val brush = Brush.linearGradient(
        colors = listOf(primaryColor, secondaryColor)
    )

    return this
        .shadow(
            elevation = glowBlur.dp,
            shape = RoundedCornerShape(cornerRadius.dp),
            ambientColor = primaryColor.copy(alpha = glowAlpha),
            spotColor = secondaryColor.copy(alpha = glowAlpha)
        )
        .background(brush, shape = RoundedCornerShape(cornerRadius.dp))
        .border(
            width = 2.dp,
            color = primaryColor.copy(alpha = glowAlpha),
            shape = RoundedCornerShape(cornerRadius.dp)
        )
}

/**
 * Modifier for animated pulsing glow effect
 */
@Composable
fun Modifier.pulsingGlow(
    pathwayTheme: PathwayTheme,
    intensity: Float = 1f
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val primaryColor = when (pathwayTheme) {
        PathwayTheme.GYM -> GymGradientStart
        PathwayTheme.DIET -> DietGradientStart
        PathwayTheme.WELLNESS -> WellnessGradientStart
    }

    return this
        .alpha(0.9f + (pulseAlpha - 0.5f) * 0.2f)
        .shadow(
            elevation = (8f * pulseAlpha * intensity).dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = primaryColor.copy(alpha = pulseAlpha * intensity),
            spotColor = primaryColor.copy(alpha = pulseAlpha * intensity)
        )
}

/**
 * Modifier for animated gradient background shift
 */
@Composable
fun Modifier.animatedGradientShift(
    pathwayTheme: PathwayTheme,
    duration: Int = 3000
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "gradientShift")
    
    val position by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientPosition"
    )

    val (color1, color2) = when (pathwayTheme) {
        PathwayTheme.GYM -> GymGradientStart to GymGradientEnd
        PathwayTheme.DIET -> DietGradientStart to DietGradientEnd
        PathwayTheme.WELLNESS -> WellnessGradientStart to WellnessGradientEnd
    }

    val brush = if (position < 0.5f) {
        Brush.linearGradient(colors = listOf(color1, color2))
    } else {
        Brush.linearGradient(colors = listOf(color2, color1))
    }

    return this.background(brush)
}

/**
 * Get app-wide animated gradient (Blue with Black) with glow
 */
@Composable
fun getAnimatedAppGradient(): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "appGlow")
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = androidx.compose.animation.core.tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "appShimmer"
    )

    return Brush.linearGradient(
        colors = listOf(
            AppGradientStart.copy(alpha = 0.7f),
            AppGradientStart,
            AppGradientEnd,
            AppGradientEnd.copy(alpha = 0.7f)
        ),
        start = Offset(shimmerPosition * 1000f, 0f),
        end = Offset(shimmerPosition * 1000f + 500f, 1000f)
    )
}

/**
 * Linear easing for smooth animations
 */
private object LinearEasing : androidx.compose.animation.core.Easing {
    override fun transform(fraction: Float): Float = fraction
}


