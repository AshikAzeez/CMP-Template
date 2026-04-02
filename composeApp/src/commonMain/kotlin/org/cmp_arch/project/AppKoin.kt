package org.cmp_arch.project

import org.cmp_arch.analytics.analyticsModule
import org.cmp_arch.core.PlatformContext
import org.cmp_arch.core.coreModule
import org.cmp_arch.core.database.databaseModule
import org.cmp_arch.core.logger.loggerModule
import org.cmp_arch.core.network.NetworkConfig
import org.cmp_arch.core.network.networkModule
import org.cmp_arch.data.dataModule
import org.cmp_arch.domain.domainModule
import org.cmp_arch.feature.home.homeFeatureModule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

private val koinLock = Any()

fun initKoin(
    platformContext: PlatformContext,
    config: AppConfig = resolveAppConfig(platformContext),
) {
    synchronized(koinLock) {
        if (GlobalContext.getOrNull() != null) return

        startKoin {
            modules(
                coreModule(platformContext),
                loggerModule(enableDebug = config.enableNetworkLogs),
                networkModule(
                    NetworkConfig(
                        baseUrl = config.baseUrl,
                        enableLogs = config.enableNetworkLogs,
                        enableAuth = config.enableAuth,
                        useMockEngine = config.useMockEngine,
                    ),
                ),
                databaseModule(),
                domainModule,
                dataModule(useMockData = config.useMockData),
                analyticsModule(),
                homeFeatureModule,
            )
        }
    }
}
