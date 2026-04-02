package org.cmp_arch.analytics

data class AnalyticsEvent(
    val name: String,
    val parameters: Map<String, String> = emptyMap(),
)
