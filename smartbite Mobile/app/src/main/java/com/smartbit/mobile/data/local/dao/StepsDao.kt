package com.smartbit.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smartbit.mobile.data.local.entity.StepsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps WHERE date = :date")
    suspend fun getStepsForDate(date: String): StepsEntity?

    @Query("SELECT * FROM steps ORDER BY date DESC")
    fun observeAll(): Flow<List<StepsEntity>>

    @Upsert
    suspend fun upsert(steps: StepsEntity)

    @Query("DELETE FROM steps")
    suspend fun clearAll()
}
