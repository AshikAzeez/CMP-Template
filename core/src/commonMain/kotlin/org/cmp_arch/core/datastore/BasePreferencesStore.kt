package org.cmp_arch.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

abstract class BasePreferencesStore(
    protected val dataStore: DataStore<Preferences>,
) {
    protected fun <T> observe(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { prefs -> prefs[key] }
    }

    protected suspend fun <T> readOnce(key: Preferences.Key<T>): T? {
        return dataStore.data.first()[key]
    }

    protected suspend fun <T> write(key: Preferences.Key<T>, value: T?) {
        dataStore.edit { prefs ->
            if (value == null) {
                prefs.remove(key)
            } else {
                prefs[key] = value
            }
        }
    }
}
