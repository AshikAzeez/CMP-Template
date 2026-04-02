package org.cmp_arch.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import okio.Path.Companion.toPath
import org.cmp_arch.core.datastore.BasePreferencesStore

class TemplatePreferencesStore(
    dataStore: DataStore<Preferences>,
) : BasePreferencesStore(dataStore), AuthSessionStore {

    private val lastSyncKey = longPreferencesKey("last_successful_sync")
    private val accessTokenKey = stringPreferencesKey("session_access_token")
    private val refreshTokenKey = stringPreferencesKey("session_refresh_token")
    private val expiryKey = longPreferencesKey("session_expiry_epoch_seconds")

    val lastSuccessfulSyncMillis: Flow<Long?> = observe(lastSyncKey)
    val accessToken: Flow<String?> = observe(accessTokenKey)

    suspend fun writeLastSuccessfulSync(epochMillis: Long) {
        write(lastSyncKey, epochMillis)
    }

    override suspend fun loadSession(): AuthSession? {
        val access = readOnce(accessTokenKey)
        val refresh = readOnce(refreshTokenKey)
        val expiry = readOnce(expiryKey)

        return if (access == null || refresh == null || expiry == null) {
            null
        } else {
            AuthSession(
                accessToken = access,
                refreshToken = refresh,
                expiresAtEpochSeconds = expiry,
            )
        }
    }

    override suspend fun saveSession(session: AuthSession?) {
        if (session == null) {
            write(accessTokenKey, null)
            write(refreshTokenKey, null)
            write(expiryKey, null)
            return
        }

        write(accessTokenKey, session.accessToken)
        write(refreshTokenKey, session.refreshToken)
        write(expiryKey, session.expiresAtEpochSeconds)
    }
}

fun createTemplateDataStore(context: PlatformContext): DataStore<Preferences> {
    val path = dataStoreAbsolutePath(context).toPath()
    return PreferenceDataStoreFactory.createWithPath(produceFile = { path })
}
