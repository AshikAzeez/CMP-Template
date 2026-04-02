package org.cmp_arch.core.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.cmp_arch.core.PlatformContext

private class InMemoryTemplateEntityStore : BaseRoomStore<TemplateEntityRecord>(), TemplateEntityStore {
    private val state = MutableStateFlow<List<TemplateEntityRecord>>(emptyList())

    override fun observeAll(): Flow<List<TemplateEntityRecord>> = state.asStateFlow()

    override suspend fun upsertAll(records: List<TemplateEntityRecord>) {
        val indexed = state.value.associateBy { it.id }.toMutableMap()
        records.forEach { record ->
            indexed[record.id] = record
        }
        state.value = indexed.values.sortedByDescending { it.updatedAtEpochMillis }
    }

    override suspend fun updateOne(recordId: String, block: (TemplateEntityRecord) -> TemplateEntityRecord) {
        state.value = state.value.map { record ->
            if (record.id == recordId) {
                block(record)
            } else {
                record
            }
        }
    }

    override suspend fun upsert(records: List<TemplateEntityRecord>) = upsertAll(records)

    override suspend fun update(recordId: String, block: (TemplateEntityRecord) -> TemplateEntityRecord) {
        updateOne(recordId, block)
    }
}

actual class DatabaseFactory actual constructor(
    context: PlatformContext,
) {
    private val migrationRegistry = MigrationRegistry(
        schemaVersion = 3,
        migrations = listOf(
            DatabaseMigration(1, 2, "Add template entity status column"),
            DatabaseMigration(2, 3, "Add template entity updatedAtEpochMillis column"),
        ),
    ).also { it.validate() }

    private val sharedStore = InMemoryTemplateEntityStore()

    actual fun createTemplateEntityStore(): TemplateEntityStore = sharedStore
}
