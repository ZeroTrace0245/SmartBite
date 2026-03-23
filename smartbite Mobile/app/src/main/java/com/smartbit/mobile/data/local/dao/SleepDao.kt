package com.smartbit.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smartbit.mobile.data.local.entity.SleepLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_logs ORDER BY startTime DESC")
    fun observeAll(): Flow<List<SleepLogEntity>>

    @Query("SELECT * FROM sleep_logs WHERE endTime IS NULL ORDER BY startTime DESC LIMIT 1")
    fun observeCurrentSleep(): Flow<SleepLogEntity?>

    @Upsert
    suspend fun upsert(sleep: SleepLogEntity)

    @Query("DELETE FROM sleep_logs")
    suspend fun clearAll()
}
