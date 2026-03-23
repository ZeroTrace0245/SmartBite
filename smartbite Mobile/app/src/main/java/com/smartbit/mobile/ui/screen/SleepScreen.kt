package com.smartbit.mobile.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.local.entity.SleepLogEntity
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.theme.PathwayTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    val currentSleep = repository.observeCurrentSleep()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
        
    val sleepLogs = repository.observeSleepLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
        
    private val _summary = mutableStateOf<String?>(null)
    val summary: State<String?> = _summary

    fun toggleSleep() {
        viewModelScope.launch {
            val current = currentSleep.value
            if (current == null) {
                repository.startSleep()
            } else {
                val updated = current.copy(endTime = System.currentTimeMillis())
                repository.updateSleep(updated)
                generateSummary(updated)
            }
        }
    }
    
    private fun generateSummary(log: SleepLogEntity) {
        val token = tokenManager.getToken() ?: return
        val durationHours = (log.endTime!! - log.startTime) / (1000.0 * 60 * 60)
        
        viewModelScope.launch {
            val prompt = "I slept for ${String.format("%.1f", durationHours)} hours. Give me a brief summary and analysis of my sleep."
            aiRepository.sendMessage(prompt, token).onSuccess {
                _summary.value = it
            }
        }
    }
}

@Composable
fun SleepScreen(
    currentTheme: PathwayTheme,
    viewModel: SleepViewModel = hiltViewModel()
) {
    val currentSleep by viewModel.currentSleep.collectAsState()
    val sleepLogs by viewModel.sleepLogs.collectAsState()
    val summary by viewModel.summary
    
    val isSleeping = currentSleep != null
    
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
            "Sleep Analysis",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Timer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (isSleeping) accentColor.copy(alpha = 0.1f) else Color(0xFF1D2136)),
            shape = RoundedCornerShape(24.dp),
            border = if (isSleeping) BorderStroke(2.dp, accentColor) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    if (isSleeping) Icons.Default.Bedtime else Icons.Default.BrightnessLow,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isSleeping) {
                    var elapsed by remember { mutableStateOf(0L) }
                    LaunchedEffect(Unit) {
                        while(true) {
                            elapsed = System.currentTimeMillis() - currentSleep!!.startTime
                            delay(1000)
                        }
                    }
                    Text(formatElapsed(elapsed), fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Time Asleep", color = Color.White.copy(alpha = 0.5f))
                } else {
                    Text("Ready to sleep?", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Start the timer when you head to bed", color = Color.White.copy(alpha = 0.5f))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { viewModel.toggleSleep() },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isSleeping) Color.Red.copy(alpha = 0.8f) else accentColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(if (isSleeping) "Wake Up" else "Start Sleeping", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        AnimatedVisibility(visible = summary != null) {
            Card(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Sleep Summary", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(summary ?: "", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Recent Sleep", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sleepLogs.filter { it.endTime != null }) { log ->
                SleepLogItem(log)
            }
        }
    }
}

@Composable
fun SleepLogItem(log: SleepLogEntity) {
    val durationHours = (log.endTime!! - log.startTime) / (1000.0 * 60 * 60)
    
    val (statusColor, statusIcon) = when {
        durationHours < 3 -> Color.Red to Icons.Default.Close
        durationHours < 5 -> Color.Yellow to Icons.Default.Check
        else -> Color.Green to Icons.Default.Check
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                Text(sdf.format(Date(log.startTime)), color = Color.White, fontWeight = FontWeight.Bold)
                Text("Slept for ${String.format("%.1f", durationHours)} hours", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(if (durationHours >= 6) "Healthy" else if (durationHours >= 4) "Short" else "Poor", color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

fun formatElapsed(millis: Long): String {
    val hours = millis / (1000 * 60 * 60)
    val minutes = (millis / (1000 * 60)) % 60
    val seconds = (millis / 1000) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
