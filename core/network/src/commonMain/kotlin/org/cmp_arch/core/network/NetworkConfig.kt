package org.cmp_arch.core.network

data class NetworkConfig(
    val baseUrl: String,
    val enableLogs: Boolean = true,
    val enableAuth: Boolean = true,
    val useMockEngine: Boolean = false,
    val mockArticlesPath: String = "mock/articles.json",
    val refreshPath: String = "v1/auth/refresh",
    val tokenExpirySkewSeconds: Long = 60,
)
