package com.smartbit.mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.theme.PathwayTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

data class StepsUiState(
    val steps: Int = 0,
    val isAiLoading: Boolean = false
)

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val aiLoading = MutableStateFlow(false)
    private val stepsCount = repository.observeSteps()
        .map { it.firstOrNull()?.count ?: 0 }

    val uiState = combine(stepsCount, aiLoading) { steps, loading ->
        StepsUiState(steps = steps, isAiLoading = loading)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StepsUiState())
    
    fun updateSteps(count: Int) {
        viewModelScope.launch {
            repository.updateSteps(count)
        }
    }

    fun extractSteps(
        description: String,
        onResult: (count: Int) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = tokenManager.getToken()
        if (token.isNullOrBlank()) {
            onError("Please set your GitHub token in AI Chat first.")
            return
        }

        if (description.isBlank()) return

        viewModelScope.launch {
            aiLoading.value = true
            try {
                val prompt = """
                    Analyze this activity description and extract the number of steps taken.
                    Description: "$description"
                    
                    Return ONLY a JSON block like this:
                    ```json
                    {
                      "steps": 5000
                    }
                    ```
                """.trimIndent()

                val result = aiRepository.sendMessage(prompt, token)
                
                result.onSuccess { response ->
                    withContext(Dispatchers.Default) {
                        val jsonStart = response.lastIndexOf("```json")
                        val jsonEnd = response.lastIndexOf("```")
                        
                        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                            val jsonStr = response.substring(jsonStart + 7, jsonEnd).trim()
                            val json = JSONObject(jsonStr)
                            
                            val steps = json.getInt("steps")
                            
                            withContext(Dispatchers.Main) {
                                onResult(steps)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                onError("Could not parse AI response")
                            }
                        }
                    }
                }.onFailure { error ->
                    onError(error.message ?: "AI request failed")
                }
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            } finally {
                aiLoading.value = false
            }
        }
    }
}

@Composable
fun StepsScreen(
    currentTheme: PathwayTheme,
    viewModel: StepsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val steps = uiState.steps
    
    var aiDescription by remember { mutableStateOf("") }
    var manualSteps by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val accentColor = when(currentTheme) {
        PathwayTheme.GYM -> Color(0xFFFF6B35)
        PathwayTheme.DIET -> Color(0xFF00D084)
        PathwayTheme.WELLNESS -> Color(0xFFB020D9)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E21))
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Steps Tracker",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // AI Section Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "AI Activity Log",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Text(
                        "Describe your activity to estimate steps",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = aiDescription,
                        onValueChange = { aiDescription = it },
                        placeholder = { Text("e.g. I walked for 30 minutes in the park", color = Color.Gray, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14172B),
                            unfocusedContainerColor = Color(0xFF14172B),
                            focusedIndicatorColor = accentColor,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = accentColor
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.extractSteps(
                                aiDescription,
                                onResult = { count ->
                                    viewModel.updateSteps(count)
                                    aiDescription = ""
                                    errorMessage = null
                                },
                                onError = { errorMessage = it }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        enabled = !uiState.isAiLoading && aiDescription.isNotBlank()
                    ) {
                        if (uiState.isAiLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Analyze Activity", fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    errorMessage?.let {
                        Text(it, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }

        // Manual Input Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = manualSteps,
                    onValueChange = { manualSteps = it.filter(Char::isDigit) },
                    label = { Text("Manual Steps", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val count = manualSteps.toIntOrNull()
                        if (count != null) {
                            viewModel.updateSteps(count)
                            manualSteps = ""
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.2f), contentColor = accentColor),
                    border = androidx.compose.foundation.BorderStroke(1.dp, accentColor)
                ) {
                    Text("Add", fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f),
                contentAlignment = Alignment.Center
            ) {
                // Circular progress ring
                CircularProgressIndicator(
                    progress = { steps / 10000f },
                    modifier = Modifier.fillMaxSize(0.8f),
                    color = accentColor,
                    strokeWidth = 12.dp,
                    trackColor = accentColor.copy(alpha = 0.1f),
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "$steps",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "of 10,000 steps",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = { viewModel.updateSteps(-100) },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF1D2136)),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = Color.White)
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                FilledIconButton(
                    onClick = { viewModel.updateSteps(100) },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = accentColor),
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                FilledIconButton(
                    onClick = { viewModel.updateSteps(500) },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF1D2136)),
                    modifier = Modifier.size(60.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Text("+500", fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
