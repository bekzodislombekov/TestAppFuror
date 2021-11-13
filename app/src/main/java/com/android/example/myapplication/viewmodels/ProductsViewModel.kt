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
import com.android.example.myapplication.utils.NetworkHelper
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val networkHelper: NetworkHelper,
    private val apiService: ApiService
) : ViewModel() {

    var productTypes: MutableLiveData<List<ProductTypeData>> = MutableLiveData()

    init {
        getProductTypes()
    }

    private fun getProductTypes() {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                productTypes.postValue(apiService.getProductTypes())
            }
        } else {
            productTypes.postValue(ArrayList())
        }
    }

    val products = Pager(PagingConfig(10, maxSize = 31)) {
        ProductDataSource(apiService,networkHelper)
    }.flow.cachedIn(viewModelScope)

}
