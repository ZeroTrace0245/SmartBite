package com.smartbit.mobile.data.remote

import com.smartbit.mobile.data.remote.dto.MealDto
import com.smartbit.mobile.data.remote.dto.ShoppingItemDto
import com.smartbit.mobile.data.remote.dto.WaterDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SmartBitApi {

    @GET("meals")
    suspend fun getMeals(): List<MealDto>

    @POST("meals")
    suspend fun addMeal(@Body meal: MealDto): MealDto

    @GET("water")
    suspend fun getWaterLogs(): List<WaterDto>

    @POST("water")
    suspend fun addWaterLog(@Body water: WaterDto): WaterDto

    @GET("shoppinglist")
    suspend fun getShoppingItems(): List<ShoppingItemDto>

    @POST("shoppinglist")
    suspend fun addShoppingItem(@Body item: ShoppingItemDto): ShoppingItemDto
}
