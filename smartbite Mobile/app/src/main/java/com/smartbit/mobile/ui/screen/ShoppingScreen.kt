package com.smartbit.mobile.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartbit.mobile.ui.state.PaymentStatus
import com.smartbit.mobile.ui.theme.DietGradientEnd
import com.smartbit.mobile.ui.theme.DietGradientStart
import com.smartbit.mobile.ui.theme.GymGradientEnd
import com.smartbit.mobile.ui.theme.GymGradientStart
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.WellnessGradientEnd
import com.smartbit.mobile.ui.theme.WellnessGradientStart
import com.smartbit.mobile.ui.viewmodel.ShoppingViewModel

import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults

@Composable
fun ShoppingScreen(
    pathwayTheme: PathwayTheme = PathwayTheme.WELLNESS,
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var aiDescription by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pathwayTheme) {
        viewModel.generateRecommendations(pathwayTheme)
    }

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
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Shopping list",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // AI Section Card
            item {
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
                                "AI Assistant",
                                color = Color.White,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            "Describe what you need or a goal (e.g. 'gain weight')",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = aiDescription,
                            onValueChange = { aiDescription = it },
                            placeholder = { Text("e.g. 3 bananas OR 'I need to gain weight'", color = Color.Gray, fontSize = 14.sp) },
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

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.extractShoppingItem(
                                        aiDescription,
                                        onResult = { name, qty ->
                                            itemName = name
                                            quantity = qty.toString()
                                            errorMessage = null
                                        },
                                        onError = { errorMessage = it }
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = gradientStart.copy(alpha = 0.7f)),
                                enabled = !uiState.isAiLoading && aiDescription.isNotBlank()
                            ) {
                                Text("Extract Item", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.generateAiRecommendations(
                                        aiDescription,
                                        onError = { errorMessage = it }
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = gradientStart),
                                enabled = !uiState.isAiLoading && aiDescription.isNotBlank()
                            ) {
                                if (uiState.isAiLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                } else {
                                    Text("Suggest Foods", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        errorMessage?.let {
                            Text(it, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }

            item { HorizontalDivider(color = Color.White.copy(alpha = 0.1f)) }

            item {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item name") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.White)
                )
            }

            item {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter(Char::isDigit) },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.White)
                )
            }

            item {
                GlowingButton(
                    text = "Add item",
                    onClick = {
                        val parsedQty = quantity.toIntOrNull() ?: return@GlowingButton
                        if (itemName.isNotBlank()) {
                            viewModel.addItem(itemName.trim(), parsedQty)
                            itemName = ""
                            quantity = "1"
                        }
                    },
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // AI Recommendations Section
            if (uiState.recommendations.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = gradientStart, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Recommended for you", color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                            }
                            
                            IconButton(
                                onClick = { 
                                    if (aiDescription.isNotBlank()) {
                                        viewModel.generateAiRecommendations(aiDescription, onError = { errorMessage = it })
                                    } else {
                                        viewModel.generateRecommendations(pathwayTheme)
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.recommendations) { rec ->
                                RecommendationChip(
                                    name = rec,
                                    onClick = { viewModel.addRecommendation(rec) },
                                    gradientStart = gradientStart
                                )
                            }
                        }
                    }
                }
            }

            item { HorizontalDivider(color = Color.White.copy(alpha = 0.1f)) }

            if (uiState.items.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White.copy(alpha = 0.1f), modifier = Modifier.size(80.dp))
                        Text("Your list is empty", color = Color.White.copy(alpha = 0.3f))
                    }
                }
            }

            items(uiState.items, key = { it.id }) { item ->
                ShoppingItemCard(
                    name = item.name,
                    quantity = item.quantity,
                    checked = item.checked,
                    onCheckedChange = { checked ->
                        viewModel.setChecked(item.id, checked)
                    },
                    gradientStart = gradientStart,
                    gradientEnd = gradientEnd
                )
            }

            // Payment Automation Section
            if (uiState.items.isNotEmpty()) {
                item {
                    PaymentSection(
                        status = uiState.paymentStatus,
                        onPay = viewModel::processPayment,
                        gradientStart = gradientStart,
                        gradientEnd = gradientEnd
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun RecommendationChip(
    name: String,
    onClick: () -> Unit,
    gradientStart: Color
) {
    Box(
        modifier = Modifier
            .background(gradientStart.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .border(1.dp, gradientStart.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(name, color = Color.White, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun PaymentSection(
    status: PaymentStatus,
    onPay: () -> Unit,
    gradientStart: Color,
    gradientEnd: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF14172B), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        when (status) {
            PaymentStatus.IDLE -> {
                Button(
                    onClick = onPay,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CreditCard, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Checkout & Pay Automatically", fontWeight = FontWeight.Bold)
                }
            }
            PaymentStatus.PROCESSING -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Processing payment via SmartPay...", color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
            }
            PaymentStatus.SUCCESS -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Payment Successful!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }
            }
            PaymentStatus.FAILED -> {
                Text("Payment Failed. Try again.", color = Color.Red)
            }
        }
    }
}

@Composable
private fun ShoppingItemCard(
    name: String,
    quantity: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    gradientStart: Color,
    gradientEnd: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shoppingCardGlow")
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Qty: $quantity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = gradientStart,
                    fontWeight = FontWeight.Bold
                )
            }
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = gradientStart,
                    uncheckedColor = gradientStart.copy(alpha = 0.5f)
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
