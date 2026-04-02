package org.cmp_arch.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath
import org.cmp_arch.core.datastore.BasePreferencesStore

class TemplatePreferencesStore(
    dataStore: DataStore<Preferences>,
) : BasePreferencesStore(dataStore), AuthSessionStore, ThemeSettingsStore {

    private val lastSyncKey = longPreferencesKey("last_successful_sync")
    private val accessTokenKey = stringPreferencesKey("session_access_token")
    private val refreshTokenKey = stringPreferencesKey("session_refresh_token")
    private val expiryKey = longPreferencesKey("session_expiry_epoch_seconds")
    private val themeModeKey = stringPreferencesKey("theme_mode")

    val lastSuccessfulSyncMillis: Flow<Long?> = observe(lastSyncKey)
    val accessToken: Flow<String?> = observe(accessTokenKey)
    override val themeMode: Flow<ThemeMode> = observe(themeModeKey).mapToThemeMode()

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

    override suspend fun setThemeMode(mode: ThemeMode) {
        write(themeModeKey, mode.name)
    }
}

fun createTemplateDataStore(context: PlatformContext): DataStore<Preferences> {
    val path = dataStoreAbsolutePath(context).toPath()
    return PreferenceDataStoreFactory.createWithPath(produceFile = { path })
}

private fun Flow<String?>.mapToThemeMode(): Flow<ThemeMode> {
    return map { stored ->
        when (stored) {
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            ThemeMode.DARK.name -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
}
