package com.smartbit.mobile.data.model

data class Meal(
    val id: String,
    val name: String,
    val calories: Int,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val loggedAt: String
)
