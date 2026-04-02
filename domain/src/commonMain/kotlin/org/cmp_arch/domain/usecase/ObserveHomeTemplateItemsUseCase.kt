package org.cmp_arch.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.domain.model.HomeTemplateItem
import org.cmp_arch.domain.repository.HomeTemplateRepository

class ObserveHomeTemplateItemsUseCase(
    private val repository: HomeTemplateRepository,
) {
    operator fun invoke(): Flow<List<HomeTemplateItem>> = repository.observeItems()
}
