package org.cmp_arch.core.database

/**
 * Room-ready base abstraction. Implementations can be backed by Room DAOs,
 * SQLite, or in-memory stores without changing the data layer contract.
 */
abstract class BaseRoomStore<Record : Any> {
    abstract suspend fun upsertAll(records: List<Record>)
    abstract suspend fun updateOne(recordId: String, block: (Record) -> Record)
}
