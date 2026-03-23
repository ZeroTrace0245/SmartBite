package com.smartbit.mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.theme.PathwayTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PerformanceState(
    val calories: Int = 0,
    val waterMl: Int = 0,
    val proteinG: Double = 0.0,
    val steps: Int = 0,
    val aiAnalysis: String? = null,
    val isAnalyzing: Boolean = false
)

@HiltViewModel
class PerformanceViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    val state = combine(
        repository.observeMeals(),
        repository.observeWater(),
        repository.observeSteps()
    ) { meals, water, steps ->
        PerformanceState(
            calories = meals.sumOf { it.calories },
            waterMl = water.sumOf { it.amountMl },
            proteinG = meals.sumOf { it.protein },
            steps = steps.sumOf { it.count }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PerformanceState())
    
    private val _analysis = mutableStateOf<String?>(null)
    val analysis: State<String?> = _analysis
    
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    fun analyzePerformance() {
        val token = tokenManager.getToken() ?: return
        val currentState = state.value
        
        viewModelScope.launch {
            _loading.value = true
            val prompt = """
                Analyze my daily performance and give me brief advice:
                - Calories consumed: ${currentState.calories} kcal
                - Water intake: ${currentState.waterMl} ml
                - Protein intake: ${currentState.proteinG} g
                - Steps taken: ${currentState.steps}
                
                Tell me what to do better to improve my fitness and health. Keep it concise.
            """.trimIndent()
            
            aiRepository.sendMessage(prompt, token).onSuccess {
                _analysis.value = it
            }.onFailure {
                _analysis.value = "Failed to analyze performance: ${it.message}"
            }
            _loading.value = false
        }
    }
}

@Composable
fun PerformanceScreen(
    currentTheme: PathwayTheme,
    viewModel: PerformanceViewModel = hiltViewModel()
) {
    val performanceState by viewModel.state.collectAsState()
    val analysis by viewModel.analysis
    val isLoading by viewModel.loading
    
    val accentColor = when(currentTheme) {
        PathwayTheme.GYM -> Color(0xFFFF6B35)
        PathwayTheme.DIET -> Color(0xFF00D084)
        PathwayTheme.WELLNESS -> Color(0xFFB020D9)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E21))
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Text(
            "Performance",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Calories", "${performanceState.calories}", "kcal", Icons.Default.Fireplace, accentColor, Modifier.weight(1f))
                    StatCard("Water", "${performanceState.waterMl}", "ml", Icons.Default.LocalDrink, Color(0xFF2196F3), Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Protein", "${performanceState.proteinG.toInt()}", "g", Icons.Default.FitnessCenter, Color(0xFFE91E63), Modifier.weight(1f))
                    StatCard("Steps", "${performanceState.steps}", "steps", Icons.Default.DirectionsWalk, Color(0xFFFFC107), Modifier.weight(1f))
                }
            }
            
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("AI Analysis", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            if (!isLoading) {
                                IconButton(onClick = { viewModel.analyzePerformance() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White.copy(alpha = 0.5f))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = accentColor)
                        } else if (analysis != null) {
                            Text(analysis!!, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp, lineHeight = 20.sp)
                        } else {
                            Text("Tap refresh to get AI analysis of your daily performance.", color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(unit, color = color.copy(alpha = 0.7f), fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}
