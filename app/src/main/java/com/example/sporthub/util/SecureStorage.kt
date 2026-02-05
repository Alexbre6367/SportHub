package com.example.sporthub.util

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage private constructor(context: Context) {
    companion object {
        private const val SHARED_PREFS_NAME = "secure_storage"
        private const val KEY_USER_ID = "user_id"

        @Volatile
        private var INSTANCE: SecureStorage? = null

        fun getInstance(context: Context): SecureStorage {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecureStorage(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        SHARED_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun saveUserId(userId: String) {
        sharedPreferences.edit {
            putString(KEY_USER_ID, userId)
        }
    }

    fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)
}