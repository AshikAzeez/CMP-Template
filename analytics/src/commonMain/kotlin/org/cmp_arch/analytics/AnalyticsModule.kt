package org.cmp_arch.analytics

import org.cmp_arch.core.logger.AppLogger
import org.koin.dsl.module

fun analyticsModule(
    extraTrackers: List<AnalyticsTracker> = emptyList(),
) = module {
    single<AnalyticsTracker> {
        val appLogger = get<AppLogger>()
        val trackers = buildList {
            add(ConsoleAnalyticsTracker(appLogger))
            addAll(extraTrackers)
            if (isEmpty()) add(NoOpAnalyticsTracker())
        }
        CompositeAnalyticsTracker(trackers)
    }
}
