package com.smartbit.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smartbit.mobile.data.local.entity.WaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {

    @Query("SELECT * FROM water_logs ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<WaterEntity>>

    @Query("SELECT * FROM water_logs WHERE synced = 0")
    suspend fun getPendingSync(): List<WaterEntity>

    @Upsert
    suspend fun upsertAll(items: List<WaterEntity>)

    @Upsert
    suspend fun upsert(item: WaterEntity)

    @Query("UPDATE water_logs SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<String>)

    @Query("DELETE FROM water_logs")
    suspend fun clearAll()
}
