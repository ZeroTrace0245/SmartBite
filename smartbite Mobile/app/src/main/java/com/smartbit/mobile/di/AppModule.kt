package com.smartbit.mobile.di

import android.content.Context
import androidx.room.Room
import com.smartbit.mobile.BuildConfig
import com.smartbit.mobile.data.local.AppDatabase
import com.smartbit.mobile.data.local.dao.MealDao
import com.smartbit.mobile.data.local.dao.ShoppingDao
import com.smartbit.mobile.data.local.dao.WaterDao
import com.smartbit.mobile.data.local.dao.StepsDao
import com.smartbit.mobile.data.local.dao.SleepDao
import com.smartbit.mobile.data.remote.SmartBitApi
import com.smartbit.mobile.data.remote.GitHubModelsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): SmartBitApi = retrofit.create(SmartBitApi::class.java)

    @Provides
    @Singleton
    fun provideGitHubModelsApi(client: OkHttpClient, moshi: Moshi): GitHubModelsApi =
        Retrofit.Builder()
            .baseUrl("https://models.inference.ai.azure.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GitHubModelsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "smartbit.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMealDao(db: AppDatabase): MealDao = db.mealDao()

    @Provides
    fun provideWaterDao(db: AppDatabase): WaterDao = db.waterDao()

    @Provides
    fun provideShoppingDao(db: AppDatabase): ShoppingDao = db.shoppingDao()

    @Provides
    fun provideStepsDao(db: AppDatabase): StepsDao = db.stepsDao()

    @Provides
    fun provideSleepDao(db: AppDatabase): SleepDao = db.sleepDao()
}
