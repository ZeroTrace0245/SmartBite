package com.smartbit.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_logs")
data class SleepLogEntity(
    @PrimaryKey val id: String,
    val startTime: Long, // Epoch millis
    val endTime: Long?,  // Epoch millis, null if currently sleeping
    val date: String,    // YYYY-MM-DD (start date)
    val synced: Boolean = false
)
