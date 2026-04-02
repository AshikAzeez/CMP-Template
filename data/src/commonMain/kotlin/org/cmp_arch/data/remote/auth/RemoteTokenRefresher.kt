package org.cmp_arch.data.remote.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.cmp_arch.core.AuthSession
import org.cmp_arch.core.TokenRefresher
import org.cmp_arch.core.network.NetworkConfig

class RemoteTokenRefresher(
    private val networkConfig: NetworkConfig,
) : TokenRefresher {

    private val refreshClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }

    override suspend fun refresh(refreshToken: String): AuthSession? {
        return runCatching {
            refreshClient.post(refreshEndpointUrl()) {
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }.body<RefreshTokenResponse>().toDomain()
        }.getOrNull()
    }

    private fun refreshEndpointUrl(): String {
        val base = networkConfig.baseUrl.trimEnd('/')
        val path = networkConfig.refreshPath.trimStart('/')
        return "$base/$path"
    }
}

@Serializable
private data class RefreshTokenRequest(
    val refreshToken: String,
)

@Serializable
private data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochSeconds: Long,
)

private fun RefreshTokenResponse.toDomain(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresAtEpochSeconds = expiresAtEpochSeconds,
    )
}
