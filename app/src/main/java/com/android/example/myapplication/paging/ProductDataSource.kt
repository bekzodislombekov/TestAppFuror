package com.android.example.myapplication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.example.myapplication.models.ProductData
import com.android.example.myapplication.retrofit.ApiService

class ProductDataSource(private val apiService: ApiService) : PagingSource<Int, ProductData>() {

    override fun getRefreshKey(state: PagingState<Int, ProductData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductData> {
        return try {
            val nextPageNumber = params.key ?: 1
            val products = apiService.getProducts(nextPageNumber)
            if (nextPageNumber > 1) {
                LoadResult.Page(products, nextPageNumber - 1, nextPageNumber + 1)
            } else {
                LoadResult.Page(products, null, nextPageNumber + 1)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}