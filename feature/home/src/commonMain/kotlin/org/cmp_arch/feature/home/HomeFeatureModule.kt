package org.cmp_arch.feature.home

import org.koin.dsl.module

val homeFeatureModule = module {
    factory { HomeViewModel(get(), get(), get(), get()) }
}
