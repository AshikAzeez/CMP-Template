package org.cmp_arch.core.pagination

import kotlinx.coroutines.flow.Flow

interface PaginationRequest<Filter : Any> {
    val page: Int
    val size: Int
    val filter: Filter
}

interface PaginationRepository<Item : Any, Filter : Any> {
    fun stream(filter: Filter): Flow<List<Item>>
}

abstract class BasePaginationConfig(
    open val pageSize: Int = 20,
    open val initialPage: Int = 1,
)
