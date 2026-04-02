package org.cmp_arch.data

import org.cmp_arch.core.TokenRefresher
import org.cmp_arch.data.local.HomeTemplateLocalDataSource
import org.cmp_arch.data.remote.auth.RemoteTokenRefresher
import org.cmp_arch.data.repository.HomeTemplateRepositoryImpl
import org.cmp_arch.domain.repository.HomeTemplateRepository
import org.koin.dsl.module

fun dataModule(useMockData: Boolean) = module {
    single<TokenRefresher> { RemoteTokenRefresher(get()) }
    single { HomeTemplateLocalDataSource(get()) }
    single<HomeTemplateRepository> { HomeTemplateRepositoryImpl(get(), useMockData) }
}
