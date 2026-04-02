package org.cmp_arch.domain.usecase

import org.cmp_arch.core.AppResult
import org.cmp_arch.domain.repository.HomeTemplateRepository

class SeedHomeTemplateItemsUseCase(
    private val repository: HomeTemplateRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = repository.seedItemsIfNeeded()
}
