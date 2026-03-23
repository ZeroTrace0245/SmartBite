package com.smartbit.mobile.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modern bright colors
private val BrightCyan = Color(0xFF00BCD4)
private val BrightBlue = Color(0xFF0288D1)
private val VibrantOrange = Color(0xFFFF6B35)
private val VibrantPurple = Color(0xFFB020D9)
private val FreshGreen = Color(0xFF00D084)
private val LimeGreen = Color(0xFF00E676)
private val LightBg = Color(0xFFFAFAFA)
private val CardWhite = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1A1A1A)
private val TextGray = Color(0xFF666666)

@Composable
fun LoginScreenNew(
    onCreateAccount: () -> Unit,
    onUserSignIn: () -> Unit,
    onAdminSignIn: () -> Unit,
    onStartLogging: () -> Unit,
    onManageList: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightBg, Color(0xFFF5F5F5))
                )
            )
    ) {
        // Decorative background elements
        ModernBackgroundDecor()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header Section
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -30 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Animated icon background
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(BrightCyan, VibrantPurple)
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .shadow(8.dp, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Layers,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SmartBite",
                        style = MaterialTheme.typography.displaySmall.copy(
                            brush = Brush.horizontalGradient(
                                colors = listOf(VibrantPurple, BrightCyan)
                            ),
                            fontSize = 40.sp
                        ),
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your AI-Powered Nutrition Coach",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Track meals • Monitor calories • Smart shopping • AI insights",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800, 200)) + slideInVertically(tween(800, 200)) { 30 }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Primary CTA Button
                    Button(
                        onClick = onCreateAccount,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VibrantOrange
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            "Create Account",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    // Secondary Buttons Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onUserSignIn,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrightCyan
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp))
                        ) {
                            Text(
                                "Sign In",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        Button(
                            onClick = onAdminSignIn,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VibrantPurple
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp))
                        ) {
                            Text(
                                "Admin",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Feature Cards
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000, 400)) + slideInVertically(tween(1000, 400)) { 40 }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ModernFeatureCardNew(
                        title = "📊 Meal Log",
                        description = "Track meals",
                        cta = "Start",
                        onClick = onStartLogging,
                        gradientColors = listOf(VibrantOrange, Color(0xFFFF9100)),
                        modifier = Modifier.weight(1f)
                    )
                    ModernFeatureCardNew(
                        title = "🛒 Shopping",
                        description = "Manage items",
                        cta = "Manage",
                        onClick = onManageList,
                        gradientColors = listOf(FreshGreen, LimeGreen),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CreateAccountScreenNew(
    onBack: () -> Unit,
    onAccountCreated: (Set<String>) -> Unit
) {
    var step by rememberSaveable { mutableStateOf(1) }
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showAccountError by rememberSaveable { mutableStateOf(false) }
    var showPathwayError by rememberSaveable { mutableStateOf(false) }

    val options = remember {
        listOf(
            PathwayOption("gym", "💪 Gym Member", "Workout-first plans, macros, and performance tracking."),
            PathwayOption("diet", "🥗 Diet Focus", "Structured meal plans and targeted nutrition guidance."),
            PathwayOption("wellness", "🌿 Everyday Wellness", "Balanced habits for healthy daily living.")
        )
    }

    val selectedPathways = rememberSaveable { mutableStateOf(setOf<String>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightBg, Color(0xFFF5F5F5))
                )
            )
    ) {
        ModernBackgroundDecor()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = VibrantPurple, fontWeight = FontWeight.SemiBold)
            }

            Text(
                text = if (step == 1) "Create your account" else "Choose your pathway",
                style = MaterialTheme.typography.headlineSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (step == 1) {
                    "Enter your login details first."
                } else {
                    "Select at least one pathway to continue."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = TextGray
            )

            if (step == 1) {
                // Input fields with modern styling
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedIndicatorColor = VibrantPurple,
                        unfocusedIndicatorColor = Color(0xFFE0E0E0)
                    )
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedIndicatorColor = VibrantPurple,
                        unfocusedIndicatorColor = Color(0xFFE0E0E0)
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedIndicatorColor = VibrantPurple,
                        unfocusedIndicatorColor = Color(0xFFE0E0E0)
                    )
                )

                if (showAccountError) {
                    Text(
                        text = "All login details are required before moving to pathways.",
                        color = Color(0xFFE63946),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || username.isBlank() || password.isBlank()) {
                            showAccountError = true
                        } else {
                            showAccountError = false
                            step = 2
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                ) {
                    Text("Continue", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                }
            } else {
                // Pathway selection
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { option ->
                        val selected = option.id in selectedPathways.value
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) {
                                    VibrantPurple.copy(alpha = 0.1f)
                                } else {
                                    CardWhite
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(if (selected) 4.dp else 2.dp, RoundedCornerShape(16.dp)),
                            border = if (selected) {
                                androidx.compose.foundation.BorderStroke(2.dp, VibrantPurple)
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        option.title,
                                        color = TextDark,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        option.description,
                                        color = TextGray,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 13.sp
                                    )
                                }
                                Checkbox(
                                    checked = selected,
                                    onCheckedChange = { checked ->
                                        selectedPathways.value = if (checked) {
                                            selectedPathways.value + option.id
                                        } else {
                                            selectedPathways.value - option.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                if (showPathwayError) {
                    Text(
                        text = "You must choose at least one pathway to continue.",
                        color = Color(0xFFE63946),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { step = 1 },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Previous", fontWeight = FontWeight.SemiBold, color = TextDark)
                    }

                    Button(
                        onClick = {
                            if (selectedPathways.value.isEmpty()) {
                                showPathwayError = true
                            } else {
                                showPathwayError = false
                                onAccountCreated(selectedPathways.value)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Create account", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ModernBackgroundDecor() {
    // Floating circles with soft colors
    Box(
        modifier = Modifier
            .size(200.dp)
            .align(Alignment.TopEnd)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(BrightCyan.copy(alpha = 0.15f), Color.Transparent)
                ),
                shape = RoundedCornerShape(100.dp)
            )
            .offset(x = 50.dp, y = (-50).dp)
    )

    Box(
        modifier = Modifier
            .size(150.dp)
            .align(Alignment.BottomStart)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(VibrantPurple.copy(alpha = 0.12f), Color.Transparent)
                ),
                shape = RoundedCornerShape(75.dp)
            )
            .offset(x = (-50).dp, y = 50.dp)
    )
}

@Composable
private fun ModernFeatureCardNew(
    title: String,
    description: String,
    cta: String,
    onClick: () -> Unit,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Gradient header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = gradientColors
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                fontSize = 12.sp
            )

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gradientColors[0]
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .height(40.dp)
            ) {
                Text(
                    cta,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

