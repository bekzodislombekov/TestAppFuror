package com.android.example.myapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.android.example.myapplication.models.ProductTypeData
import com.android.example.myapplication.paging.ProductDataSource
import com.android.example.myapplication.retrofit.ApiService
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val apiService: ApiService
) : ViewModel() {

    var productTypes: MutableLiveData<List<ProductTypeData>> = MutableLiveData()

    init {
        getProductTypes()
    }

    private fun getProductTypes() = viewModelScope.launch {
        productTypes.postValue(apiService.getProductTypes())
    }

    val products = Pager(PagingConfig(10, maxSize = 31)) {
        ProductDataSource(apiService)
    }.flow.cachedIn(viewModelScope)


}
