package com.smartbit.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.state.MealsUiState
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
class MealsViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val syncing = MutableStateFlow(false)
    private val aiLoading = MutableStateFlow(false)

    val uiState = combine(repository.observeMeals(), syncing, aiLoading) { meals, isSyncing, isAiLoading ->
        MealsUiState(
            meals = meals, 
            isSyncing = isSyncing,
            isAiLoading = isAiLoading
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MealsUiState())

    init {
        refresh()
    }

    fun addMeal(
        name: String,
        calories: Int,
        protein: Double = 0.0,
        fat: Double = 0.0,
        carbs: Double = 0.0
    ) {
        viewModelScope.launch {
            syncing.update { true }
            repository.addMeal(name, calories, protein, fat, carbs)
            syncing.update { false }
        }
    }

    fun extractMacros(
        description: String,
        onResult: (name: String, calories: Int, protein: Double, fat: Double, carbs: Double) -> Unit,
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
                    Analyze this meal description and provide nutrition facts. 
                    Description: "$description"
                    
                    Return ONLY a JSON block like this:
                    ```json
                    {
                      "name": "Brief summary",
                      "calories": 350,
                      "protein": 25.5,
                      "fat": 10.0,
                      "carbs": 40.0
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
                            
                            val name = json.getString("name")
                            val calories = json.getInt("calories")
                            val protein = json.optDouble("protein", 0.0)
                            val fat = json.optDouble("fat", 0.0)
                            val carbs = json.optDouble("carbs", 0.0)
                            
                            withContext(Dispatchers.Main) {
                                onResult(name, calories, protein, fat, carbs)
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
