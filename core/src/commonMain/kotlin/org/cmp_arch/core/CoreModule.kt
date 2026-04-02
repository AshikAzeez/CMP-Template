package org.cmp_arch.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module

fun coreModule(platformContext: PlatformContext) = module {
    single<PlatformContext> { platformContext }
    single<AppDispatchers> { DefaultAppDispatchers }
    single<DataStore<Preferences>> { createTemplateDataStore(get()) }
    single { TemplatePreferencesStore(get()) }
    single<AuthSessionStore> { get<TemplatePreferencesStore>() }
    single<ThemeSettingsStore> { get<TemplatePreferencesStore>() }
}
