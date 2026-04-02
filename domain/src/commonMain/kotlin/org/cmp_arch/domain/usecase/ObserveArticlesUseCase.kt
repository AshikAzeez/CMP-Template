package org.cmp_arch.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.domain.model.Article
import org.cmp_arch.domain.repository.ArticlesRepository

class ObserveArticlesUseCase(
    private val repository: ArticlesRepository,
) {
    operator fun invoke(): Flow<List<Article>> = repository.observeArticles()
}
