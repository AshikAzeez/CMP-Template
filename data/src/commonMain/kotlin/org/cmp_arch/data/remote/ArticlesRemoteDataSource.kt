package org.cmp_arch.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.cmp_arch.core.AppResult
import org.cmp_arch.core.network.safeNetworkCall

interface ArticlesRemoteDataSource {
    suspend fun getArticles(): AppResult<List<ArticleDto>>
}

class KtorArticlesRemoteDataSource(
    private val httpClient: HttpClient,
) : ArticlesRemoteDataSource {

    override suspend fun getArticles(): AppResult<List<ArticleDto>> {
        return safeNetworkCall {
            httpClient.get("v1/articles").body<List<ArticleDto>>()
        }
    }
}
