package org.example.project.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.project.AppContextHolder

actual class DataStoreFactory {
    actual fun getDataStore(): DataStore<Preferences> = createDataStore(
        producePath = { AppContextHolder.context.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}