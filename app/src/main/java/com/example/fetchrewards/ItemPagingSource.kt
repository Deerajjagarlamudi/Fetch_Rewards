package com.example.fetchrewards



import androidx.paging.PagingSource
import androidx.paging.PagingState

class ItemPagingSource(
    private val api: FetchApiService
) : PagingSource<Int, UiItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiItem> {
        return try {
            val allItems = api.getItems()
                .filter { !it.name.isNullOrBlank() }
                .sortedWith(compareBy({ it.listId }, { it.name }))

            val grouped = allItems.groupBy { it.listId }
            val flatList = mutableListOf<UiItem>()

            for ((listId, items) in grouped) {
                flatList.add(UiItem.Header(listId))
                items.forEach { flatList.add(UiItem.Content(it)) }
            }

            val page = params.key ?: 1
            val pageSize = params.loadSize
            val from = (page - 1) * pageSize
            val to = minOf(from + pageSize, flatList.size)

            val pagedItems = if (from < flatList.size) flatList.subList(from, to) else emptyList()

            LoadResult.Page(
                data = pagedItems,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (to >= flatList.size) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UiItem>): Int? = 1
}

sealed class UiItem {
    data class Header(val listId: Int) : UiItem()
    data class Content(val item: Item) : UiItem()
}