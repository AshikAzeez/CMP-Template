package org.cmp_arch.domain.usecase

import org.cmp_arch.core.AppResult
import org.cmp_arch.domain.repository.ArticlesRepository

class RefreshArticlesUseCase(
    private val repository: ArticlesRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = repository.refreshArticles()
}
