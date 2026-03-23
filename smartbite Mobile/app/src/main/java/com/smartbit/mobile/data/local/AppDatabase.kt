package com.smartbit.mobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smartbit.mobile.data.local.dao.MealDao
import com.smartbit.mobile.data.local.dao.ShoppingDao
import com.smartbit.mobile.data.local.dao.WaterDao
import com.smartbit.mobile.data.local.dao.StepsDao
import com.smartbit.mobile.data.local.dao.SleepDao
import com.smartbit.mobile.data.local.entity.MealEntity
import com.smartbit.mobile.data.local.entity.ShoppingItemEntity
import com.smartbit.mobile.data.local.entity.WaterEntity
import com.smartbit.mobile.data.local.entity.StepsEntity
import com.smartbit.mobile.data.local.entity.SleepLogEntity

@Database(
    entities = [
        MealEntity::class, 
        WaterEntity::class, 
        ShoppingItemEntity::class,
        StepsEntity::class,
        SleepLogEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun waterDao(): WaterDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun stepsDao(): StepsDao
    abstract fun sleepDao(): SleepDao
}
