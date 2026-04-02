package org.cmp_arch.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.cmp_arch.core.AppError
import org.cmp_arch.core.AppResult
import org.cmp_arch.core.database.TemplateEntityRecord
import org.cmp_arch.data.local.HomeTemplateLocalDataSource
import org.cmp_arch.domain.model.HomeTemplateItem
import org.cmp_arch.domain.model.ItemStatus
import org.cmp_arch.domain.repository.HomeTemplateRepository

class HomeTemplateRepositoryImpl(
    private val localDataSource: HomeTemplateLocalDataSource,
    private val useMockSeed: Boolean,
) : HomeTemplateRepository {

    override fun observeItems() = localDataSource.observeItems().map { records ->
        records.map { it.toDomain() }
    }

    override suspend fun seedItemsIfNeeded(): AppResult<Unit> {
        return runCatching {
            val currentItems = localDataSource.observeItems().first()
            if (currentItems.isEmpty()) {
                localDataSource.upsertItems(seedItems(useMockSeed))
            }
            Unit
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { throwable -> AppResult.Failure(AppError.Unknown(throwable)) },
        )
    }
}

private fun TemplateEntityRecord.toDomain(): HomeTemplateItem {
    return HomeTemplateItem(
        id = id,
        title = title,
        description = description,
        status = runCatching { ItemStatus.valueOf(status) }.getOrDefault(ItemStatus.TODO),
        updatedAtEpochMillis = updatedAtEpochMillis,
    )
}

private fun seedItems(useMockSeed: Boolean): List<TemplateEntityRecord> {
    val base = listOf(
        TemplateEntityRecord(
            id = "layer-separation",
            title = "Keep Layers Isolated",
            description = "Presentation depends on domain contracts, not concrete data implementations.",
            status = ItemStatus.DONE.name,
            updatedAtEpochMillis = 1_740_000_001_000,
        ),
        TemplateEntityRecord(
            id = "intent-first",
            title = "Use Intent-First Events",
            description = "Every user action is represented as an intent and processed in one reducer path.",
            status = ItemStatus.IN_PROGRESS.name,
            updatedAtEpochMillis = 1_740_000_002_000,
        ),
        TemplateEntityRecord(
            id = "immutable-state",
            title = "Expose Immutable State",
            description = "UI observes a single immutable state object backed by StateFlow.",
            status = ItemStatus.TODO.name,
            updatedAtEpochMillis = 1_740_000_003_000,
        ),
        TemplateEntityRecord(
            id = "effects-channel",
            title = "Emit One-Off Effects",
            description = "Navigation, snackbars, and toasts are emitted as effects, not persisted in UI state.",
            status = ItemStatus.TODO.name,
            updatedAtEpochMillis = 1_740_000_004_000,
        ),
    )

    if (!useMockSeed) return base

    return base + TemplateEntityRecord(
        id = "feature-onboarding",
        title = "Copy This Vertical Slice",
        description = "Duplicate Home module structure to bootstrap a new feature in minutes.",
        status = ItemStatus.TODO.name,
        updatedAtEpochMillis = 1_740_000_005_000,
    )
}
