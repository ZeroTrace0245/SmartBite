package com.smartbit.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterEntity(
    @PrimaryKey val id: String,
    val amountMl: Int,
    val loggedAt: String,
    val synced: Boolean
)
