package org.cmp_arch.domain

import org.cmp_arch.domain.usecase.ObserveArticlesUseCase
import org.cmp_arch.domain.usecase.RefreshArticlesUseCase
import org.cmp_arch.domain.usecase.UpdateBookmarkUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ObserveArticlesUseCase(get()) }
    factory { RefreshArticlesUseCase(get()) }
    factory { UpdateBookmarkUseCase(get()) }
}
