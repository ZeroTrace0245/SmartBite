package com.smartbit.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smartbit.mobile.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meals ORDER BY loggedAt DESC")
    fun observeAll(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE synced = 0")
    suspend fun getPendingSync(): List<MealEntity>

    @Upsert
    suspend fun upsertAll(items: List<MealEntity>)

    @Upsert
    suspend fun upsert(item: MealEntity)

    @Query("UPDATE meals SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<String>)

    @Query("DELETE FROM meals")
    suspend fun clearAll()
}
