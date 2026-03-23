package com.smartbit.mobile.ui.screen

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartbit.mobile.ui.theme.DietGradientEnd
import com.smartbit.mobile.ui.theme.DietGradientStart
import com.smartbit.mobile.ui.theme.GymGradientEnd
import com.smartbit.mobile.ui.theme.GymGradientStart
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.WellnessGradientEnd
import com.smartbit.mobile.ui.theme.WellnessGradientStart
import com.smartbit.mobile.ui.viewmodel.ChatViewModel

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun ChatScreen(
    pathwayTheme: PathwayTheme = PathwayTheme.WELLNESS,
    viewModel: ChatViewModel = hiltViewModel()
) {
    var inputText by remember { mutableStateOf("") }
    var showTokenDialog by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages = uiState.messages
    val isLoading = uiState.isLoading
    val error = uiState.error
    val hasToken = uiState.hasToken

    val (primaryColor, _) = when (pathwayTheme) {
        PathwayTheme.GYM -> GymGradientStart to GymGradientEnd
        PathwayTheme.DIET -> DietGradientStart to DietGradientEnd
        PathwayTheme.WELLNESS -> WellnessGradientStart to WellnessGradientEnd
    }

    val listState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Show token dialog only if no token exists
    LaunchedEffect(hasToken) {
        if (!hasToken && !showTokenDialog) {
            showTokenDialog = true
        }
    }

    if (showTokenDialog) {
        TokenDialog(
            onTokenEntered = { token ->
                viewModel.setToken(token)
                showTokenDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E21)) // Dark navy base
            .statusBarsPadding() // Address the "white bar" issue
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Modern Header
            ChatHeader(
                primaryColor = primaryColor,
                hasToken = hasToken,
                onTokenClick = { showTokenDialog = true },
                onClearClick = { viewModel.clearChat() }
            )

            // Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                items(messages, key = { it.id }) { message ->
                    ModernMessageBubble(
                        message = message,
                        primaryColor = primaryColor
                    )
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            ThinkingIndicator(primaryColor)
                        }
                    }
                }

                if (error != null) {
                    item {
                        ErrorCard(error)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Input Area
            ChatInputArea(
                inputText = inputText,
                onTextChange = { inputText = it },
                onSend = {
                    if (hasToken) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    } else {
                        showTokenDialog = true
                    }
                },
                primaryColor = primaryColor,
                isLoading = isLoading
            )
        }
    }
}

@Composable
private fun ChatHeader(
    primaryColor: Color,
    hasToken: Boolean,
    onTokenClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = primaryColor.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "AI Coach",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (hasToken) Color(0xFF51CF66) else Color.Red)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (hasToken) "Active" else "Token Required",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
        
        Row {
            IconButton(onClick = onClearClick) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "Clear Chat", tint = Color.White.copy(alpha = 0.6f))
            }
            IconButton(onClick = onTokenClick) {
                Icon(Icons.Default.Settings, contentDescription = "Token Settings", tint = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
private fun ModernMessageBubble(
    message: ChatMessage,
    primaryColor: Color
) {
    val isUser = message.isUser
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = primaryColor.copy(alpha = 0.1f),
                modifier = Modifier.size(32.dp).align(Alignment.Bottom)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) primaryColor else Color(0xFF1D2136)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text(
                text = parseMarkdown(message.text),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                lineHeight = 20.sp
            )
        }
    }
}

private fun parseMarkdown(text: String): AnnotatedString {
    // 1. Remove JSON blocks from display (they are used for auto-logging in ViewModel)
    val jsonStartIndex = text.indexOf("```json")
    val jsonEndIndex = text.indexOf("```", if (jsonStartIndex != -1) jsonStartIndex + 7 else 0)
    
    val cleanText = if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
        val before = text.substring(0, jsonStartIndex).trim()
        val after = if (jsonEndIndex + 3 < text.length) text.substring(jsonEndIndex + 3).trim() else ""
        (before + "\n" + after).trim()
    } else {
        text
    }

    return buildAnnotatedString {
        val lines = cleanText.split("\n")
        lines.forEachIndexed { index, line ->
            val currentLine = line
            
            // Handle headers
            when {
                currentLine.startsWith("### ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.White)) {
                        append(currentLine.removePrefix("### "))
                    }
                }
                currentLine.startsWith("## ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp, color = Color.White)) {
                        append(currentLine.removePrefix("## "))
                    }
                }
                currentLine.startsWith("# ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp, color = Color.White)) {
                        append(currentLine.removePrefix("# "))
                    }
                }
                else -> {
                    // Handle bold **text**
                    var remaining = currentLine
                    while (remaining.contains("**")) {
                        val start = remaining.indexOf("**")
                        append(remaining.substring(0, start))
                        val end = remaining.indexOf("**", start + 2)
                        if (end != -1) {
                            withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, color = Color.White)) {
                                append(remaining.substring(start + 2, end))
                            }
                            remaining = remaining.substring(end + 2)
                        } else {
                            append("**")
                            remaining = remaining.substring(start + 2)
                        }
                    }
                    append(remaining)
                }
            }
            
            if (index < lines.size - 1) {
                append("\n")
            }
        }
    }
}

@Composable
private fun ChatInputArea(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    primaryColor: Color,
    isLoading: Boolean
) {
    Surface(
        color = Color(0xFF14172B),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onTextChange,
                placeholder = { Text("Ask anything...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1D2136),
                    unfocusedContainerColor = Color(0xFF1D2136),
                    focusedIndicatorColor = primaryColor.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = primaryColor
                ),
                singleLine = false
            )

            IconButton(
                onClick = onSend,
                enabled = inputText.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (inputText.isNotBlank()) primaryColor else Color.Gray.copy(alpha = 0.3f),
                        CircleShape
                    )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThinkingIndicator(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .background(Color(0xFF1D2136), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Thinking", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Row {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = dotAlpha))
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(error: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF6B6B).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFF6B6B).copy(alpha = 0.5f))
    ) {
        Text(
            "Error: $error",
            color = Color(0xFFFF6B6B),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun TokenDialog(
    onTokenEntered: (String) -> Unit
) {
    var token by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("AI Access Token") },
        containerColor = Color(0xFF1D2136),
        titleContentColor = Color.White,
        textContentColor = Color.White.copy(alpha = 0.7f),
        text = {
            Column {
                Text("Enter your GitHub Personal Access Token to talk with the AI Coach.")
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text("Token") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF14172B),
                        unfocusedContainerColor = Color(0xFF14172B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onTokenEntered(token) }, enabled = token.isNotBlank()) {
                Text("Save Token", color = Color.Cyan)
            }
        }
    )
}
