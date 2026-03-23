package com.smartbit.mobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepsEntity(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val count: Int,
    val synced: Boolean = false
)
