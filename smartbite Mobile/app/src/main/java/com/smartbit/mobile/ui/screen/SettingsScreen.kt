package com.smartbit.mobile.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.theme.PathwayTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    val hasToken = mutableStateOf(tokenManager.hasToken())
    
    fun resetData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
    
    fun logout(onLogout: () -> Unit) {
        // Clear session if needed
        onLogout()
    }
    
    fun deleteToken() {
        tokenManager.clearToken()
        hasToken.value = false
    }
    
    fun saveToken(token: String) {
        tokenManager.saveToken(token)
        hasToken.value = true
    }
}

@Composable
fun SettingsScreen(
    currentTheme: PathwayTheme,
    onThemeSelected: (PathwayTheme) -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var showTokenDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    
    val accentColor = when(currentTheme) {
        PathwayTheme.GYM -> Color(0xFFFF6B35)
        PathwayTheme.DIET -> Color(0xFF00D084)
        PathwayTheme.WELLNESS -> Color(0xFFB020D9)
    }

    if (showTokenDialog) {
        TokenUpdateDialog(
            onDismiss = { showTokenDialog = false },
            onSave = { 
                viewModel.saveToken(it)
                showTokenDialog = false
            }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { 
                onThemeSelected(it)
                showThemeDialog = false
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data?") },
            text = { Text("This will permanently delete all your logged meals, water, steps, and sleep data. Your AI token will remain safe.") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.resetData()
                    showResetDialog = false
                }) {
                    Text("Reset", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E21))
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Profile Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.2f),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("SmartBite User", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Active Member", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { SettingHeader("Preferences") }
            item {
                SettingItem(
                    icon = Icons.Default.Palette,
                    title = "Change Pathway Theme",
                    subtitle = "Current: ${currentTheme.name.lowercase().capitalize()}",
                    onClick = { showThemeDialog = true }
                )
            }
            item {
                SettingItem(
                    icon = Icons.Default.Sync,
                    title = "Sync Data Now",
                    subtitle = "Last synced: Just now",
                    onClick = { /* Trigger sync */ }
                )
            }
            
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { SettingHeader("AI & Security") }
            item {
                SettingItem(
                    icon = Icons.Default.Key,
                    title = "AI Access Token",
                    subtitle = if (viewModel.hasToken.value) "Token configured" else "No token set",
                    onClick = { showTokenDialog = true }
                )
            }
            if (viewModel.hasToken.value) {
                item {
                    SettingItem(
                        icon = Icons.Default.DeleteForever,
                        title = "Delete AI Token",
                        subtitle = "Remove your GitHub token from the app",
                        onClick = { viewModel.deleteToken() },
                        titleColor = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { SettingHeader("Account") }
            item {
                SettingItem(
                    icon = Icons.Default.Refresh,
                    title = "Reset App Data",
                    subtitle = "Clear all local logs (meals, water, etc.)",
                    onClick = { showResetDialog = true }
                )
            }
            item {
                SettingItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    subtitle = "Sign out of your account",
                    onClick = { viewModel.logout(onLogout) },
                    titleColor = Color.Red
                )
            }
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: PathwayTheme,
    onDismiss: () -> Unit,
    onThemeSelected: (PathwayTheme) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Pathway Theme", color = Color.White) },
        containerColor = Color(0xFF1D2136),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PathwayTheme.entries.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (currentTheme == theme) Color.White.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { onThemeSelected(theme) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val color = when(theme) {
                            PathwayTheme.GYM -> Color(0xFFFF6B35)
                            PathwayTheme.DIET -> Color(0xFF00D084)
                            PathwayTheme.WELLNESS -> Color(0xFFB020D9)
                        }
                        Box(modifier = Modifier.size(16.dp).background(color, CircleShape))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = theme.name.lowercase().capitalize(),
                            color = if (currentTheme == theme) color else Color.White,
                            fontWeight = if (currentTheme == theme) FontWeight.Bold else FontWeight.Normal
                        )
                        if (currentTheme == theme) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.Check, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.White.copy(alpha = 0.6f))
            }
        }
    )
}

@Composable
fun SettingHeader(text: String) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.4f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = Color.White
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2136).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = titleColor, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(subtitle, color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.2f))
        }
    }
}

@Composable
fun TokenUpdateDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var token by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update GitHub Token") },
        text = {
            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Enter New Token") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onSave(token) }, enabled = token.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
