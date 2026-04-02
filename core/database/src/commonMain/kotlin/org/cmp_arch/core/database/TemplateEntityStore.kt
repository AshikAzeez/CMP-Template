package org.cmp_arch.core.database

import kotlinx.coroutines.flow.Flow

/**
 * Generic storage contract for feature seed/content entities.
 * Data layer maps these records to domain models per feature.
 */
interface TemplateEntityStore {
    fun observeAll(): Flow<List<TemplateEntityRecord>>
    suspend fun upsert(records: List<TemplateEntityRecord>)
    suspend fun update(recordId: String, block: (TemplateEntityRecord) -> TemplateEntityRecord)
}

data class TemplateEntityRecord(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val updatedAtEpochMillis: Long,
)
