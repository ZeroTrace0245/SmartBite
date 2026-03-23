package com.smartbit.mobile.ui.state

data class DashboardUiState(
    val mealsCount: Int = 0,
    val waterTotalMl: Int = 0,
    val shoppingPendingCount: Int = 0,
    val isRefreshing: Boolean = false
)
