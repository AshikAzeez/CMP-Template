package org.cmp_arch.domain.model

data class HomeTemplateItem(
    val id: String,
    val title: String,
    val description: String,
    val status: ItemStatus,
    val updatedAtEpochMillis: Long,
)

enum class ItemStatus {
    TODO,
    IN_PROGRESS,
    DONE,
}
