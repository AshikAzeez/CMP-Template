package org.cmp_arch.domain.repository

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.core.AppResult
import org.cmp_arch.domain.model.HomeTemplateItem

interface HomeTemplateRepository {
    fun observeItems(): Flow<List<HomeTemplateItem>>
    suspend fun seedItemsIfNeeded(): AppResult<Unit>
}
