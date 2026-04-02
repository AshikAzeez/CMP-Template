package org.cmp_arch.core.database

import org.cmp_arch.core.PlatformContext
import org.koin.dsl.module

fun databaseModule() = module {
    single { DatabaseFactory(get<PlatformContext>()) }
    single<ArticleCache> { get<DatabaseFactory>().createArticleCache() }
}
