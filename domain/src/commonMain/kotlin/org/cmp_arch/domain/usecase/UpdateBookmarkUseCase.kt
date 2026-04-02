package org.cmp_arch.domain.usecase

import org.cmp_arch.core.AppResult
import org.cmp_arch.domain.repository.ArticlesRepository

class UpdateBookmarkUseCase(
    private val repository: ArticlesRepository,
) {
    suspend operator fun invoke(articleId: String, bookmarked: Boolean): AppResult<Unit> {
        return repository.updateBookmark(articleId, bookmarked)
    }
}
