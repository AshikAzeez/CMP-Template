package org.cmp_arch.core

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtEpochSeconds: Long,
)

interface AuthSessionStore {
    suspend fun loadSession(): AuthSession?
    suspend fun saveSession(session: AuthSession?)
}

interface TokenRefresher {
    suspend fun refresh(refreshToken: String): AuthSession?
}

object NoOpTokenRefresher : TokenRefresher {
    override suspend fun refresh(refreshToken: String): AuthSession? = null
}
