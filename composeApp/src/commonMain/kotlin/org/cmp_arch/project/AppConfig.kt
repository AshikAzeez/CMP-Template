package org.cmp_arch.project

data class AppConfig(
    val environmentName: String = "dev",
    val baseUrl: String = "https://mock.local/",
    val enableNetworkLogs: Boolean = true,
    val useMockData: Boolean = true,
    val useMockEngine: Boolean = true,
    val enableAuth: Boolean = false,
)
