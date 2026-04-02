package org.cmp_arch.core.network.mock

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String,
)

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochSeconds: Long,
)
