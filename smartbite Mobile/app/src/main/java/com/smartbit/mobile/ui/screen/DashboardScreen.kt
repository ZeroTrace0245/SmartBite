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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartbit.mobile.ui.theme.DietGradientEnd
import com.smartbit.mobile.ui.theme.DietGradientStart
import com.smartbit.mobile.ui.theme.GymGradientEnd
import com.smartbit.mobile.ui.theme.GymGradientStart
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.WellnessGradientEnd
import com.smartbit.mobile.ui.theme.WellnessGradientStart
import com.smartbit.mobile.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    pathwayTheme: PathwayTheme,
    onOpenPathwayPreferences: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pathwayCards = pathwayContent(pathwayTheme)

    // Get gradient colors based on pathway
    val (gradientStart, gradientEnd) = when (pathwayTheme) {
        PathwayTheme.GYM -> GymGradientStart to GymGradientEnd
        PathwayTheme.DIET -> DietGradientStart to DietGradientEnd
        PathwayTheme.WELLNESS -> WellnessGradientStart to WellnessGradientEnd
    }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Daily snapshot",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                StatCard(
                    title = "Meals logged",
                    value = uiState.mealsCount.toString(),
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd
                )
            }

            item {
                StatCard(
                    title = "Water consumed",
                    value = "${uiState.waterTotalMl} ml",
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd
                )
            }

            item {
                StatCard(
                    title = "Shopping remaining",
                    value = uiState.shoppingPendingCount.toString(),
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlowingButton(
                        text = "Sync now",
                        onClick = viewModel::refresh,
                        gradientStart = gradientStart,
                        gradientEnd = gradientEnd
                    )

                    GlowingButton(
                        text = "Pathway preferences",
                        onClick = onOpenPathwayPreferences,
                        gradientStart = gradientStart,
                        gradientEnd = gradientEnd
                    )
                }
            }

            item {
                Text(
                    pathwayHeadline(pathwayTheme),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            items(pathwayCards) { card ->
                PathwayFeatureCard(
                    title = card.title,
                    body = card.body,
                    metric = card.metric,
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd
                )
            }

            if (uiState.isRefreshing) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = gradientStart)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    gradientStart: Color,
    gradientEnd: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "statCardGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = gradientStart.copy(alpha = glowAlpha),
                spotColor = gradientEnd.copy(alpha = glowAlpha)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(gradientStart.copy(alpha = 0.15f), gradientEnd.copy(alpha = 0.15f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = gradientStart.copy(alpha = glowAlpha * 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = gradientStart,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PathwayFeatureCard(
    title: String,
    body: String,
    metric: String,
    gradientStart: Color,
    gradientEnd: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "featureCardGlow")
    
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPosition"
    )

    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = gradientStart.copy(alpha = borderAlpha),
                spotColor = gradientEnd.copy(alpha = borderAlpha)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        gradientStart.copy(alpha = 0.12f),
                        gradientEnd.copy(alpha = 0.12f)
                    ),
                    start = Offset(shimmerPosition * 1000f, 0f),
                    end = Offset(shimmerPosition * 1000f + 500f, 1000f)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = gradientStart.copy(alpha = borderAlpha * 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                body,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                metric,
                style = MaterialTheme.typography.bodySmall,
                color = gradientStart,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GlowingButton(
    text: String,
    onClick: () -> Unit,
    gradientStart: Color,
    gradientEnd: Color
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
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = gradientStart.copy(alpha = glowAlpha),
                spotColor = gradientEnd.copy(alpha = glowAlpha)
            )
            .border(
                width = 2.dp,
                color = gradientStart.copy(alpha = glowAlpha),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = gradientStart.copy(alpha = 0.8f),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class PathwayCardData(
    val title: String,
    val body: String,
    val metric: String
)

private fun pathwayHeadline(pathwayTheme: PathwayTheme): String = when (pathwayTheme) {
    PathwayTheme.GYM -> "Gym Performance Dashboard"
    PathwayTheme.DIET -> "Diet Planning Dashboard"
    PathwayTheme.WELLNESS -> "Wellness Lifestyle Dashboard"
}

private fun pathwayContent(pathwayTheme: PathwayTheme): List<PathwayCardData> = when (pathwayTheme) {
    PathwayTheme.GYM -> listOf(
        PathwayCardData(
            title = "Daily and Weekly Workout Plan",
            body = "Push day today. Weekly split: Push, Pull, Legs, Mobility, Core.",
            metric = "Completion pace: 4/5 planned sessions"
        ),
        PathwayCardData(
            title = "Calories Burned vs Intake",
            body = "You burned 2,350 kcal and consumed 2,110 kcal in the last 24h.",
            metric = "Net balance: -240 kcal"
        ),
        PathwayCardData(
            title = "Supplement Reminders",
            body = "Creatine at 8:00 AM, protein shake post workout, magnesium at night.",
            metric = "Today: 2/3 reminders completed"
        ),
        PathwayCardData(
            title = "Progress Forecast",
            body = "At your current consistency, projected upper-body strength gain is strong.",
            metric = "Forecast: +10% strength in 4 weeks"
        )
    )

    PathwayTheme.DIET -> listOf(
        PathwayCardData(
            title = "Auto-generated Weekly Meal Plan",
            body = "A 7-day meal plan is generated based on your calorie and macro targets.",
            metric = "Target adherence: 91%"
        ),
        PathwayCardData(
            title = "Portion-based Logging Shortcuts",
            body = "Quick add portions using cups, spoons, slices, and handful units.",
            metric = "Shortcuts used: 18 entries this week"
        ),
        PathwayCardData(
            title = "Nutrient Balance Insights",
            body = "Protein, carbs, and fat averages are tracked against your goals.",
            metric = "Avg split: 35% protein, 40% carbs, 25% fat"
        ),
        PathwayCardData(
            title = "Grocery List Auto-Generation",
            body = "Ingredients from your meal plan are auto-added and tied to delivery services.",
            metric = "Delivery-ready items: 14"
        )
    )

    PathwayTheme.WELLNESS -> listOf(
        PathwayCardData(
            title = "Lifestyle Goals Plan",
            body = "Simple plans for daily hydration, sleep consistency, and step goals.",
            metric = "Hydration: 82%, Sleep: 76%, Steps: 88%"
        ),
        PathwayCardData(
            title = "Habit Suggestions",
            body = "Try stretching, mindfulness, and evening walks to build better habits.",
            metric = "New suggestions this week: 6"
        ),
        PathwayCardData(
            title = "Weekly Wellness Summaries",
            body = "Your trend report summarizes energy, mood, activity, and recovery.",
            metric = "Overall wellness score: 8.2/10"
        ),
        PathwayCardData(
            title = "Gamified Challenges",
            body = "Earn badges and protect your streak by completing simple daily tasks.",
            metric = "Current streak: 11 days"
        )
    )
}

private object LinearEasing : androidx.compose.animation.core.Easing {
    override fun transform(fraction: Float): Float = fraction
}

