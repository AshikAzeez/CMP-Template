package org.cmp_arch.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.cmp_arch.core.AuthSession
import org.cmp_arch.core.AuthSessionStore
import org.cmp_arch.core.TokenRefresher
import org.cmp_arch.core.logger.AppLogger

fun createHttpClient(
    config: NetworkConfig,
    sessionStore: AuthSessionStore,
    tokenRefresher: TokenRefresher,
    resourceLoader: NetworkResourceLoader,
    logger: AppLogger,
): HttpClient {
    return if (config.useMockEngine) {
        HttpClient(
            MockEngine { request ->
                val path = request.url.encodedPath
                when {
                    path.endsWith("/v1/articles") || path == "/v1/articles" -> {
                        val payload = resourceLoader.readText(config.mockArticlesPath) ?: "[]"
                        respond(
                            content = payload,
                            status = HttpStatusCode.OK,
                            headers = headersOf("Content-Type", "application/json"),
                        )
                    }

                    path.endsWith("/${config.refreshPath.trimStart('/')}") ||
                        path == "/${config.refreshPath.trimStart('/')}" -> {
                        respond(
                            content = """
                                {
                                  \"accessToken\": \"mock-access-token\",
                                  \"refreshToken\": \"mock-refresh-token\",
                                  \"expiresAtEpochSeconds\": ${currentEpochSeconds() + 3600}
                                }
                            """.trimIndent(),
                            status = HttpStatusCode.OK,
                            headers = headersOf("Content-Type", "application/json"),
                        )
                    }

                    else -> {
                        respond(
                            content = "{\"message\":\"Not Found\"}",
                            status = HttpStatusCode.NotFound,
                            headers = headersOf("Content-Type", "application/json"),
                        )
                    }
                }
            },
        ) {
            installCommon(config, sessionStore, tokenRefresher, logger)
        }
    } else {
        HttpClient {
            installCommon(config, sessionStore, tokenRefresher, logger)
        }
    }
}

private fun io.ktor.client.HttpClientConfig<*>.installCommon(
    config: NetworkConfig,
    sessionStore: AuthSessionStore,
    tokenRefresher: TokenRefresher,
    appLogger: AppLogger,
) {
    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                isLenient = true
            },
        )
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
    }

    if (config.enableLogs) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    appLogger.debug(tag = "Ktor", message = message)
                }
            }
            level = LogLevel.HEADERS
        }
    }

    if (config.enableAuth) {
        install(Auth) {
            bearer {
                loadTokens {
                    val session = runBlocking { sessionStore.loadSession() }
                    session?.toBearerTokensIfValid(config.tokenExpirySkewSeconds)
                }
                refreshTokens {
                    refreshUsingStore(
                        sessionStore = sessionStore,
                        tokenRefresher = tokenRefresher,
                        skewSeconds = config.tokenExpirySkewSeconds,
                    )
                }
            }
        }
    }

    defaultRequest {
        url(config.baseUrl)
        contentType(ContentType.Application.Json)
    }
}

private fun RefreshTokensParams.refreshUsingStore(
    sessionStore: AuthSessionStore,
    tokenRefresher: TokenRefresher,
    skewSeconds: Long,
): BearerTokens? {
    val currentSession = runBlocking { sessionStore.loadSession() } ?: return null
    val refreshedSession = runBlocking { tokenRefresher.refresh(currentSession.refreshToken) }
    runBlocking { sessionStore.saveSession(refreshedSession) }
    return refreshedSession?.toBearerTokensIfValid(skewSeconds)
}

private fun AuthSession.toBearerTokensIfValid(skewSeconds: Long): BearerTokens? {
    val now = currentEpochSeconds()
    if (expiresAtEpochSeconds - skewSeconds <= now) return null
    return BearerTokens(accessToken, refreshToken)
}
