package com.app.gw2_pvp_hub.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.gw2_pvp_hub.data.models.ApiKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.Preferences as Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class UserPreferences @Inject constructor(
    private val context: Context
){

    companion object {
        val API_KEY_NAME = stringPreferencesKey("API_KEY_NAME")
        val API_KEY_VALUE = stringPreferencesKey("API_KEY_VALUE")
    }

    suspend fun saveApiKey(apiKey: ApiKey) {
        context.dataStore.edit {
            it[API_KEY_NAME] = apiKey.name.toString()
            it[API_KEY_VALUE] = apiKey.value.toString()
        }
    }

    suspend fun getApiKey(): Any? {
        val value = context.dataStore.data
            .map {
                it[API_KEY_VALUE]
            }
        return value.firstOrNull()
    }
}
