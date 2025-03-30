package org.example.project.store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.uitls.JsonUtil

object DataStoreManager {

    val dataStoreFactory = DataStoreFactory().getDataStore()

    suspend inline fun <reified T : Any> putSerializable(key: String, value: T) {
        dataStoreFactory.edit { prefs ->
            prefs.set(key = stringPreferencesKey(key), value = JsonUtil.toJson(value))
        }
    }

    inline fun <reified T : Any> getSerializable(key: String): Flow<T?> =
        dataStoreFactory.data.map {
            val string = it[stringPreferencesKey(key)]?.toString()
            if (string.isNullOrEmpty()) {
                null
            } else {
                JsonUtil.fromJson(string)
            }
        }

    suspend fun putString(key: String, value: String) {
        dataStoreFactory.edit { prefs ->
            prefs.set(key = stringPreferencesKey(key), value = value)
        }
    }

    fun getString(key: String): Flow<String?> =
        dataStoreFactory.data.map {
            it[stringPreferencesKey(key)]?.toString()
        }

    suspend fun putBoolean(key: String, value: Boolean) {
        dataStoreFactory.edit { prefs ->
            prefs.set(key = booleanPreferencesKey(key), value = value)
        }
    }

    fun getBoolean(key: String): Flow<Boolean> =
        dataStoreFactory.data.map {
            it[booleanPreferencesKey(key)] == true
        }

    suspend fun putInt(key: String, value: Int) {
        dataStoreFactory.edit { prefs ->
            prefs.set(key = intPreferencesKey(key), value = value)
        }
    }

    fun getInt(key: String): Flow<Int?> =
        dataStoreFactory.data.map {
            it[stringPreferencesKey(key)]?.toInt()
        }

    suspend fun putFloat(key: String, value: Float) {
        dataStoreFactory.edit { prefs ->
            prefs.set(key = floatPreferencesKey(key), value = value)
        }
    }

    fun getFloat(key: String): Flow<Float?> =
        dataStoreFactory.data.map {
            it[floatPreferencesKey(key)]?.toFloat()
        }

}