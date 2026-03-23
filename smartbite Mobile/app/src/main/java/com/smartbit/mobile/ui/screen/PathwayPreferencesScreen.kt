package com.smartbit.mobile.ui.screen

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartbit.mobile.ui.theme.DietGradientEnd
import com.smartbit.mobile.ui.theme.DietGradientStart
import com.smartbit.mobile.ui.theme.GymGradientEnd
import com.smartbit.mobile.ui.theme.GymGradientStart
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.WellnessGradientEnd
import com.smartbit.mobile.ui.theme.WellnessGradientStart

@Composable
fun PathwayPreferencesScreen(
    currentTheme: PathwayTheme,
    onBack: () -> Unit,
    onThemeChosen: (PathwayTheme) -> Unit
) {
    var selectedTheme by rememberSaveable { mutableStateOf(currentTheme) }

    val options = listOf(
        ThemeOption(PathwayTheme.GYM, "Gym Member", "Red theme", GymGradientStart, GymGradientEnd),
        ThemeOption(PathwayTheme.DIET, "Diet Focus", "Green theme", DietGradientStart, DietGradientEnd),
        ThemeOption(PathwayTheme.WELLNESS, "Everyday Wellness", "Purple theme", WellnessGradientStart, WellnessGradientEnd)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E27),
                        Color(0xFF1A1F3A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = Color.White)
            }

            Text(
                text = "Pathway Preferences",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Choose a pathway theme for your app experience.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )

            options.forEach { option ->
                ThemeOptionCard(
                    option = option,
                    isSelected = selectedTheme == option.theme,
                    onSelect = { selectedTheme = option.theme }
                )
            }

            GlowingButton(
                text = "Apply Theme",
                onClick = { onThemeChosen(selectedTheme) },
                gradientStart = when (selectedTheme) {
                    PathwayTheme.GYM -> GymGradientStart
                    PathwayTheme.DIET -> DietGradientStart
                    PathwayTheme.WELLNESS -> WellnessGradientStart
                },
                gradientEnd = when (selectedTheme) {
                    PathwayTheme.GYM -> GymGradientEnd
                    PathwayTheme.DIET -> DietGradientEnd
                    PathwayTheme.WELLNESS -> WellnessGradientEnd
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ThemeOptionCard(
    option: ThemeOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "themeCardGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.7f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 16.dp else 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = option.gradientStart.copy(alpha = glowAlpha),
                spotColor = option.gradientEnd.copy(alpha = glowAlpha)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        option.gradientStart.copy(alpha = if (isSelected) 0.2f else 0.1f),
                        option.gradientEnd.copy(alpha = if (isSelected) 0.2f else 0.1f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 2.5.dp else 1.5.dp,
                color = option.gradientStart.copy(alpha = if (isSelected) glowAlpha else glowAlpha * 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    option.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    option.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = option.gradientStart,
                    unselectedColor = option.gradientStart.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun GlowingButton(
    text: String,
    onClick: () -> Unit,
    gradientStart: Color,
    gradientEnd: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "buttonGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonGlowAlpha"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = gradientStart.copy(alpha = glowAlpha),
                spotColor = gradientEnd.copy(alpha = glowAlpha)
            )
            .border(
                width = 2.dp,
                color = gradientStart.copy(alpha = glowAlpha),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = gradientStart.copy(alpha = 0.8f),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

private data class ThemeOption(
    val theme: PathwayTheme,
    val title: String,
    val subtitle: String,
    val gradientStart: Color,
    val gradientEnd: Color
)
