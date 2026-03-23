package com.smartbit.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.smartbit.mobile.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM shopping_items ORDER BY name ASC")
    fun observeAll(): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE synced = 0")
    suspend fun getPendingSync(): List<ShoppingItemEntity>

    @Upsert
    suspend fun upsertAll(items: List<ShoppingItemEntity>)

    @Upsert
    suspend fun upsert(item: ShoppingItemEntity)

    @Query("UPDATE shopping_items SET checked = :checked, synced = 0 WHERE id = :id")
    suspend fun setChecked(id: String, checked: Boolean)

    @Query("UPDATE shopping_items SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<String>)

    @Query("DELETE FROM shopping_items")
    suspend fun clearAll()
}
