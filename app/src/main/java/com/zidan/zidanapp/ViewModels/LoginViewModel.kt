package com.zidan.zidanapp.ViewModels

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import com.zidan.zidanapp.Data.datastore
import kotlinx.coroutines.flow.first

class LoginViewModel(app: Application): AndroidViewModel(app) {
    private val dataStore: DataStore<Preferences> = app.applicationContext.datastore

    // Login status
    suspend fun setLoginStatus(loginStatus: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey("loginStatus")] = loginStatus
        }
    }
    suspend fun getLoginStatus(): Boolean? {
        return dataStore.data.first()[booleanPreferencesKey("loginStatus")]
    }

    // Username
    suspend fun setRememberedUsername(rememberedUsername: String) {
        dataStore.edit {
            it[stringPreferencesKey("rememberedUsername")] = rememberedUsername
        }
    }

    suspend fun getRememberedUsername(): String? {
        return dataStore.data.first()[stringPreferencesKey("rememberedUsername")]
    }

    // Password
    suspend fun setRememberedPassword(rememberedPassword: String) {
        dataStore.edit {
            it[stringPreferencesKey("rememberedPassword")] = rememberedPassword
        }
    }
    suspend fun getRememberedPassword(): String? {
        return dataStore.data.first()[stringPreferencesKey("rememberedPassword")]
    }
}