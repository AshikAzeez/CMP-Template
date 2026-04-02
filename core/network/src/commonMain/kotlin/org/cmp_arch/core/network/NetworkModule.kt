package org.cmp_arch.core.network

import io.ktor.client.HttpClient
import org.cmp_arch.core.AuthSessionStore
import org.cmp_arch.core.PlatformContext
import org.cmp_arch.core.TokenRefresher
import org.cmp_arch.core.logger.AppLogger
import org.koin.dsl.module

fun networkModule(config: NetworkConfig) = module {
    single { config }
    single { NetworkResourceLoader(get<PlatformContext>()) }
    single<HttpClient> { createHttpClient(get(), get<AuthSessionStore>(), get<TokenRefresher>(), get(), get<AppLogger>()) }
}
