package com.smartbit.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.manager.TokenManager
import com.smartbit.mobile.data.repository.GitHubModelsRepository
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.state.ShoppingUiState
import com.smartbit.mobile.ui.state.PaymentStatus
import com.smartbit.mobile.ui.theme.PathwayTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: SmartBitRepository,
    private val aiRepository: GitHubModelsRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val syncing = MutableStateFlow(false)
    private val aiLoading = MutableStateFlow(false)
    private val recommendations = MutableStateFlow<List<String>>(emptyList())
    private val paymentStatus = MutableStateFlow(PaymentStatus.IDLE)

    val uiState = combine(repository.observeShopping(), syncing, recommendations, paymentStatus, aiLoading) { items, isSyncing, recs, status, isAiLoading ->
        ShoppingUiState(
            items = items, 
            isSyncing = isSyncing, 
            recommendations = recs,
            paymentStatus = status,
            isAiLoading = isAiLoading
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShoppingUiState())

    init {
        refresh()
    }

    fun addItem(name: String, quantity: Int) {
        viewModelScope.launch {
            syncing.update { true }
            repository.addShoppingItem(name, quantity)
            syncing.update { false }
        }
    }

    fun extractShoppingItem(
        description: String,
        onResult: (name: String, quantity: Int) -> Unit,
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
                    Analyze this shopping item description and extract the item name and quantity.
                    Description: "$description"
                    
                    Return ONLY a JSON block like this:
                    ```json
                    {
                      "name": "Apples",
                      "quantity": 5
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
                            val quantity = json.optInt("quantity", 1)
                            
                            withContext(Dispatchers.Main) {
                                onResult(name, quantity)
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

    fun generateAiRecommendations(
        goal: String,
        onError: (String) -> Unit
    ) {
        val token = tokenManager.getToken()
        if (token.isNullOrBlank()) {
            onError("Please set your GitHub token in AI Chat first.")
            return
        }

        if (goal.isBlank()) return

        viewModelScope.launch {
            aiLoading.update { true }
            try {
                val prompt = """
                    As a nutrition assistant, recommend exactly 5 shopping items for the user's goal.
                    Goal: "$goal"
                    
                    Return ONLY a JSON block like this:
                    ```json
                    {
                      "recommendations": ["Item 1", "Item 2", "Item 3", "Item 4", "Item 5"]
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
                            
                            val recsArray = json.getJSONArray("recommendations")
                            val recsList = mutableListOf<String>()
                            for (i in 0 until recsArray.length()) {
                                recsList.add(recsArray.getString(i))
                            }
                            
                            withContext(Dispatchers.Main) {
                                recommendations.value = recsList
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

    fun setChecked(id: String, checked: Boolean) {
        viewModelScope.launch {
            syncing.update { true }
            repository.updateShoppingItemChecked(id, checked)
            syncing.update { false }
        }
    }

    fun generateRecommendations(pathway: PathwayTheme) {
        viewModelScope.launch {
            syncing.update { true }
            // Simulating AI recommendation logic based on pathway
            val recs = when (pathway) {
                PathwayTheme.GYM -> listOf("Whey Protein", "Creatine Monohydrate", "Oats", "Chicken Breast", "Peanut Butter")
                PathwayTheme.DIET -> listOf("Avocado", "Quinoa", "Spinach", "Greek Yogurt", "Almonds")
                PathwayTheme.WELLNESS -> listOf("Chamomile Tea", "Dark Chocolate (85%)", "Blueberries", "Walnuts", "Green Tea")
            }
            delay(1000) // Simulate AI processing
            recommendations.value = recs
            syncing.update { false }
        }
    }

    fun addRecommendation(name: String) {
        addItem(name, 1)
        recommendations.update { it.filter { item -> item != name } }
    }

    fun processPayment() {
        viewModelScope.launch {
            paymentStatus.value = PaymentStatus.PROCESSING
            delay(2000) // Simulate payment gateway interaction
            paymentStatus.value = PaymentStatus.SUCCESS
            delay(1500) // Show success message for a bit
            paymentStatus.value = PaymentStatus.IDLE
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
