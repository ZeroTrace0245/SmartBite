package com.smartbit.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbit.mobile.data.repository.SmartBitRepository
import com.smartbit.mobile.ui.state.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SmartBitRepository
) : ViewModel() {

    private val refreshing = MutableStateFlow(false)

    val uiState = combine(
        repository.observeMeals(),
        repository.observeWater(),
        repository.observeShopping(),
        refreshing
    ) { meals, water, shopping, isRefreshing ->
        DashboardUiState(
            mealsCount = meals.size,
            waterTotalMl = water.sumOf { it.amountMl },
            shoppingPendingCount = shopping.count { !it.checked },
            isRefreshing = isRefreshing
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    fun refresh() {
        viewModelScope.launch {
            refreshing.update { true }
            repository.refreshAll()
            repository.syncPending()
            refreshing.update { false }
        }
    }
}
