package com.smartbit.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.state.WaterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val syncing = MutableStateFlow(false)
    private val aiLoading = MutableStateFlow(false)

    val uiState = combine(repository.observeWater(), syncing, aiLoading) { logs, isSyncing, isAiLoading ->
        WaterUiState(waterLogs = logs, isSyncing = isSyncing, isAiLoading = isAiLoading)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WaterUiState())

    init {
        refresh()
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            syncing.update { true }
            repository.addWaterLog(amountMl)
            syncing.update { false }
        }
    }

    fun extractWater(
        description: String,
        onResult: (amountMl: Int) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = tokenManager.getToken()
        if (token.isNullOrBlank()) {
            onError("Please set your GitHub token in AI Chat first.")
            return
        }

        if (description.isBlank()) return

        viewModelScope.launch {
            aiLoading.update { true }
            try {
                val prompt = """
                    Analyze this hydration/water description and extract the amount in milliliters (ml).
                    Description: "$description"
                    
                    Return ONLY a JSON block like this:
                    ```json
                    {
                      "amountMl": 250
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
                            
                            val amountMl = json.getInt("amountMl")
                            
                            withContext(Dispatchers.Main) {
                                onResult(amountMl)
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
                aiLoading.update { false }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            syncing.update { true }
            repository.refreshAll()
            repository.syncPending()
            syncing.update { false }
        }
    }
}
