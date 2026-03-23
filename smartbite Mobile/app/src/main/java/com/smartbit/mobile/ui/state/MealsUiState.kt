package com.smartbit.mobile.ui.state

import com.smartbit.mobile.data.model.Meal

data class MealsUiState(
    val meals: List<Meal> = emptyList(),
    val isSyncing: Boolean = false,
    val isAiLoading: Boolean = false
)
