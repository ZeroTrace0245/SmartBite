package com.smartbit.mobile.ui.state

import com.smartbit.mobile.data.model.ShoppingItem

data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),
    val isSyncing: Boolean = false,
    val recommendations: List<String> = emptyList(),
    val paymentStatus: PaymentStatus = PaymentStatus.IDLE,
    val isAiLoading: Boolean = false
)

enum class PaymentStatus {
    IDLE, PROCESSING, SUCCESS, FAILED
}
