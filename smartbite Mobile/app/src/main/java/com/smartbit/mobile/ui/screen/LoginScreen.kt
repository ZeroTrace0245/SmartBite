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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.smartbit.mobile.ui.theme.*

// Modern bright colors for login
// These are currently unused as this screen is the "Old" version

@Composable
fun LoginScreenOld(
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
            .background(NavyBase)
    ) {
        BackgroundLayers()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { -40 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Layers,
                        contentDescription = null,
                        tint = NeonBlue,
                        modifier = Modifier.height(52.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "SmartBite",
                        style = MaterialTheme.typography.displaySmall.copy(
                            brush = Brush.horizontalGradient(
                                colors = listOf(NeonBlue, Color(0xFFB8C7FF), Color.White)
                            )
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Automate diet tracking, monitor calories, manage your shopping list with ease, and get tailored insights from your AI Coach.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000, 200)) + slideInVertically(tween(1000, 200)) { 40 }
            ) {
                Column {
                    Button(
                        onClick = onCreateAccount,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E74C8)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text("Create Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = onUserSignIn,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                        ) {
                            Text("Sign In", color = Color(0xFFB8C7FF), fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onAdminSignIn,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF17192D)),
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                        ) {
                            Text("Admin Sign In", color = Color(0xFFD3DBFF), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1200, 400)) + slideInVertically(tween(1200, 400)) { 60 }
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    FeatureCard(
                        title = "Meal Logging",
                        description = "Keep track of every meal and monitor your daily calorie intake.",
                        cta = "Start Logging",
                        onClick = onStartLogging,
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        title = "Shopping List",
                        description = "Organize your grocery shopping and ensure you never miss an item.",
                        cta = "Manage List",
                        onClick = onManageList,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateAccountScreenOld(
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
            PathwayOption("gym", "Gym Member", "Workout-first plans, macros, and performance tracking."),
            PathwayOption("diet", "Diet Focus", "Structured meal plans and targeted nutrition guidance."),
            PathwayOption("wellness", "Everyday Wellness", "Balanced habits for healthy daily living.")
        )
    }

    val selectedPathways = rememberSaveable { mutableStateOf(setOf<String>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBase)
    ) {
        BackgroundLayers()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Back", color = Color(0xFFD3DBFF))
            }

            Text(
                text = if (step == 1) "Create your account" else "Choose your pathway",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFB8C7FF),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (step == 1) {
                    "Enter your login details first."
                } else {
                    "Select at least one pathway to continue."
                },
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted
            )

            if (step == 1) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showAccountError) {
                    Text(
                        text = "All login details are required before moving to pathways.",
                        color = Color(0xFFFFA5A5),
                        style = MaterialTheme.typography.bodyMedium
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E74C8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("Continue", fontWeight = FontWeight.SemiBold)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    options.forEach { option ->
                        val selected = option.id in selectedPathways.value
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selected) Color(0xFF274F95) else CardSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        option.title,
                                        color = Color(0xFFD9E2FF),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        option.description,
                                        color = TextMuted,
                                        style = MaterialTheme.typography.bodyMedium
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
                        color = Color(0xFFFFA5A5),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { step = 1 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Previous")
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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E74C8)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Create account", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.BackgroundLayers() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(NavyBase, Color(0xFF06266A)),
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .align(Alignment.TopEnd)
            .clip(RoundedCornerShape(bottomStart = 220.dp))
            .background(NavyLayerOne.copy(alpha = alpha1))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .height(560.dp)
            .align(Alignment.BottomStart)
            .clip(RoundedCornerShape(topEnd = 220.dp))
            .background(NavyLayerTwo.copy(alpha = alpha2))
    )
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    cta: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Color(0xFFB8C7FF), fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            OutlinedButton(onClick = onClick, shape = RoundedCornerShape(10.dp)) {
                Text(cta, color = NeonBlue, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
