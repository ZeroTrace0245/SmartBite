package com.smartbit.mobile.data.manager

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun hasToken(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    fun clearToken() {
        sharedPreferences.edit().apply {
            remove(KEY_TOKEN)
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "smartbite_prefs"
        private const val KEY_TOKEN = "github_token"
    }
}
