package org.cmp_arch.core.network

data class NetworkConfig(
    val baseUrl: String,
    val enableLogs: Boolean = true,
    val enableAuth: Boolean = true,
    val useMockEngine: Boolean = false,
    val mockTemplateItemsPath: String = "mock/template_items.json",
    val refreshPath: String = "v1/auth/refresh",
    val tokenExpirySkewSeconds: Long = 60,
)
