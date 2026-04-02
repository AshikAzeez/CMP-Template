package org.cmp_arch.core.logger

import org.koin.dsl.module

fun loggerModule(enableDebug: Boolean) = module {
    single<AppLogger> {
        initNapier(enableDebug = enableDebug)
        NapierAppLogger()
    }
}
