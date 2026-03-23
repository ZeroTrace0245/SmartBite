package com.smartbit.mobile.ui.state

import com.smartbit.mobile.data.model.WaterLog

data class WaterUiState(
    val waterLogs: List<WaterLog> = emptyList(),
    val isSyncing: Boolean = false,
    val isAiLoading: Boolean = false
)
