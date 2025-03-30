package org.example.project.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory {
    actual fun getDataStore(): DataStore<Preferences> {
        TODO("Not yet implemented")
    }
}