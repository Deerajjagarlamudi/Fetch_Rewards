package com.example.fetchrewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class ItemViewModel : ViewModel() {

    val pagedItems: Flow<PagingData<UiItem>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { ItemPagingSource(RetrofitInstance.api) }
    ).flow.cachedIn(viewModelScope)
}