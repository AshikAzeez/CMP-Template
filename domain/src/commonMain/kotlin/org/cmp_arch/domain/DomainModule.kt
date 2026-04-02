package org.cmp_arch.domain

import org.cmp_arch.domain.usecase.ObserveHomeTemplateItemsUseCase
import org.cmp_arch.domain.usecase.SeedHomeTemplateItemsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ObserveHomeTemplateItemsUseCase(get()) }
    factory { SeedHomeTemplateItemsUseCase(get()) }
}
