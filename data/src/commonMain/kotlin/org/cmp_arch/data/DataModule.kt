package org.cmp_arch.data

import org.cmp_arch.data.local.ArticlesLocalDataSource
import org.cmp_arch.data.remote.ArticlesRemoteDataSource
import org.cmp_arch.data.remote.KtorArticlesRemoteDataSource
import org.cmp_arch.data.remote.MockArticlesRemoteDataSource
import org.cmp_arch.data.remote.auth.RemoteTokenRefresher
import org.cmp_arch.core.TokenRefresher
import org.cmp_arch.data.repository.ArticlesRepositoryImpl
import org.cmp_arch.domain.repository.ArticlesRepository
import org.koin.dsl.module

fun dataModule(useMockData: Boolean) = module {
    single<ArticlesRemoteDataSource> {
        if (useMockData) {
            MockArticlesRemoteDataSource()
        } else {
            KtorArticlesRemoteDataSource(get())
        }
    }
    single<TokenRefresher> { RemoteTokenRefresher(get()) }
    single { ArticlesLocalDataSource(get()) }
    single<ArticlesRepository> { ArticlesRepositoryImpl(get(), get(), get()) }
}
