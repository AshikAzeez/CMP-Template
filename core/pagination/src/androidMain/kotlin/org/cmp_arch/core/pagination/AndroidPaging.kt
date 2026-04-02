package org.cmp_arch.core.pagination

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow

abstract class BasePagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {
    final override fun getRefreshKey(state: PagingState<Key, Value>): Key? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey ?: state.closestPageToPosition(anchor)?.nextKey
        }
    }
}

inline fun <Value : Any> createPager(
    pageSize: Int = 20,
    crossinline source: () -> PagingSource<Int, Value>,
): Flow<PagingData<Value>> {
    return Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = { source() },
    ).flow
}
