package com.smartbit.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.screen.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasToken: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: GitHubModelsRepository,
    private val smartBitRepository: SmartBitRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Check if token exists on init
        val hasToken = tokenManager.hasToken()
        
        // Add system prompt for fitness coaching and meal analysis
        repository.addSystemPrompt(
            """You are a knowledgeable and friendly fitness and nutrition coach for the SmartBite app. 
            Your role is to:
            1. Provide helpful, accurate advice about nutrition, workouts, and health.
            2. ANALYZE MEAL DESCRIPTIONS: If a user describes what they ate (e.g., "I ate 3 chicken breasts with greek yogurt"), you MUST estimate the nutrition and provide a JSON block at the VERY END of your response.
            
            JSON format for meal analysis:
            ```json
            {
              "type": "meal_log",
              "name": "Brief summary of the meal",
              "calories": total_calories_as_int,
              "protein": protein_grams_as_double,
              "fat": fat_grams_as_double,
              "carbs": carb_grams_as_double
            }
            ```
            If there are multiple items, sum them up into one meal log.
            
            Always be encouraging and personalized. Keep answers concise but informative."""
        )

        // Add welcome message
        _uiState.update {
            it.copy(
                messages = listOf(
                    ChatMessage(
                        text = "🤖 Welcome to SmartBite AI Coach!\n\nI'm here to help you with nutrition, fitness, and health goals. Ask me anything about meal planning, workouts, macros, or your fitness journey!",
                        isUser = false
                    )
                ),
                hasToken = hasToken
            )
        }
    }

    fun sendMessage(messageText: String) {
        val token = tokenManager.getToken()
        
        if (messageText.isBlank()) return
        if (token.isNullOrBlank()) {
            _uiState.update {
                it.copy(error = "Token not set. Please set your GitHub token.")
            }
            return
        }

        viewModelScope.launch {
            try {
                // Add user message
                _uiState.update {
                    it.copy(
                        messages = it.messages + ChatMessage(text = messageText, isUser = true),
                        isLoading = true,
                        error = null
                    )
                }

                // Get AI response
                val result = repository.sendMessage(messageText, token)

                result.onSuccess { aiResponse ->
                    _uiState.update {
                        it.copy(
                            messages = it.messages + ChatMessage(
                                text = aiResponse,
                                isUser = false
                            ),
                            isLoading = false
                        )
                    }
                    parseMealLog(aiResponse)
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to get response"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    private fun parseMealLog(response: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.Default) {
                    val jsonStart = response.lastIndexOf("```json")
                    val jsonEnd = response.lastIndexOf("```")
                    
                    if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                        val jsonStr = response.substring(jsonStart + 7, jsonEnd).trim()
                        val json = JSONObject(jsonStr)
                        
                        if (json.optString("type") == "meal_log") {
                            val name = json.getString("name")
                            val calories = json.getInt("calories")
                            val protein = json.optDouble("protein", 0.0)
                            val fat = json.optDouble("fat", 0.0)
                            val carbs = json.optDouble("carbs", 0.0)
                            
                            smartBitRepository.addMeal(
                                name = name,
                                calories = calories,
                                protein = protein,
                                fat = fat,
                                carbs = carbs
                            )
                            
                            // Add a system confirmation message on the main thread
                            withContext(Dispatchers.Main) {
                                _uiState.update {
                                    it.copy(
                                        messages = it.messages + ChatMessage(
                                            text = "✅ Automatically logged: $name ($calories kcal, P:${protein}g, F:${fat}g, C:${carbs}g)",
                                            isUser = false
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Silently fail if JSON is malformed or not present
                e.printStackTrace()
            }
        }
    }

    fun setToken(token: String) {
        if (token.isNotBlank()) {
            tokenManager.saveToken(token)
            _uiState.update { it.copy(hasToken = true) }
        }
    }

    fun clearToken() {
        tokenManager.clearToken()
        _uiState.update { it.copy(hasToken = false, error = "Token cleared") }
    }

    fun clearChat() {
        repository.clearHistory()
        _uiState.update {
            it.copy(
                messages = listOf(
                    ChatMessage(
                        text = "🤖 Welcome to SmartBite AI Coach!\n\nI'm here to help you with nutrition, fitness, and health goals. Ask me anything about meal planning, workouts, macros, or your fitness journey!",
                        isUser = false
                    )
                ),
                error = null
            )
        }
    }
}
