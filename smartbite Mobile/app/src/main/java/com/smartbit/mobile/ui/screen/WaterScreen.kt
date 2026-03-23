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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.smartbit.mobile.ui.theme.DietGradientEnd
import com.smartbit.mobile.ui.theme.DietGradientStart
import com.smartbit.mobile.ui.theme.GymGradientEnd
import com.smartbit.mobile.ui.theme.GymGradientStart
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.WellnessGradientEnd
import com.smartbit.mobile.ui.theme.WellnessGradientStart
import com.smartbit.mobile.ui.viewmodel.WaterViewModel

@Composable
fun WaterScreen(
    pathwayTheme: PathwayTheme = PathwayTheme.WELLNESS,
    viewModel: WaterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var amount by remember { mutableStateOf("250") }
    var aiDescription by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Water Tracker",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // AI Section Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1D2136), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = gradientStart,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "AI Auto-Log",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        "Describe your hydration and let AI extract the volume",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = aiDescription,
                        onValueChange = { aiDescription = it },
                        placeholder = { Text("e.g. I drank a big bottle of water", color = Color.Gray, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14172B),
                            unfocusedContainerColor = Color(0xFF14172B),
                            focusedIndicatorColor = gradientStart,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = gradientStart
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.extractWater(
                                aiDescription,
                                onResult = { ml ->
                                    amount = ml.toString()
                                    errorMessage = null
                                },
                                onError = { errorMessage = it }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = gradientStart),
                        enabled = !uiState.isAiLoading && aiDescription.isNotBlank()
                    ) {
                        if (uiState.isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Extract Amount", fontWeight = FontWeight.Bold)
                        }
                    }

                    errorMessage?.let {
                        Text(it, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter(Char::isDigit) },
                label = { Text("Amount in ml") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.White)
            )

            GlowingButton(
                text = "Log water",
                onClick = {
                    val parsed = amount.toIntOrNull() ?: return@GlowingButton
                    viewModel.addWater(parsed)
                },
                gradientStart = gradientStart,
                gradientEnd = gradientEnd,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiState.waterLogs, key = { it.id }) { item ->
                    WaterLogCard(
                        amount = "${item.amountMl} ml",
                        time = item.loggedAt,
                        gradientStart = gradientStart,
                        gradientEnd = gradientEnd
                    )
                }
            }
        }
    }
}

@Composable
private fun WaterLogCard(
    amount: String,
    time: String,
    gradientStart: Color,
    gradientEnd: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waterCardGlow")
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
            .height(80.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = gradientStart.copy(alpha = glowAlpha),
                spotColor = gradientEnd.copy(alpha = glowAlpha)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(gradientStart.copy(alpha = 0.15f), gradientEnd.copy(alpha = 0.15f))
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = gradientStart.copy(alpha = glowAlpha * 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                amount,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                time,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
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
