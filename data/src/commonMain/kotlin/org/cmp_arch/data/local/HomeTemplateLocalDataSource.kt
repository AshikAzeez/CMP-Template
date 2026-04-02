package org.cmp_arch.data.local

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.core.database.TemplateEntityRecord
import org.cmp_arch.core.database.TemplateEntityStore

class HomeTemplateLocalDataSource(
    private val store: TemplateEntityStore,
) {
    fun observeItems(): Flow<List<TemplateEntityRecord>> = store.observeAll()

    suspend fun upsertItems(records: List<TemplateEntityRecord>) {
        store.upsert(records)
    }
}
