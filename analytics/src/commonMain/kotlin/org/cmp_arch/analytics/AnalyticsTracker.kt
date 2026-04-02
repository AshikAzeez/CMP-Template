package org.cmp_arch.analytics

import org.cmp_arch.core.logger.AppLogger

interface AnalyticsTracker {
    fun track(event: AnalyticsEvent)
    fun setUserId(userId: String?)
    fun setUserProperty(key: String, value: String)
}

class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
    override fun setUserProperty(key: String, value: String) = Unit
}

class CompositeAnalyticsTracker(
    private val delegates: List<AnalyticsTracker>,
) : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) {
        delegates.forEach { it.track(event) }
    }

    override fun setUserId(userId: String?) {
        delegates.forEach { it.setUserId(userId) }
    }

    override fun setUserProperty(key: String, value: String) {
        delegates.forEach { it.setUserProperty(key, value) }
    }
}

class ConsoleAnalyticsTracker(
    private val logger: AppLogger,
) : AnalyticsTracker {
    override fun track(event: AnalyticsEvent) {
        logger.info(tag = "Analytics", message = "${event.name} -> ${event.parameters}")
    }

    override fun setUserId(userId: String?) {
        logger.info(tag = "Analytics", message = "userId=$userId")
    }

    override fun setUserProperty(key: String, value: String) {
        logger.info(tag = "Analytics", message = "property $key=$value")
    }
}
