package com.android.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.myapplication.retrofit.ApiService
import com.android.example.myapplication.utils.NetworkHelper

class ViewModelFactory(private val apiService: ApiService,private val networkHelper: NetworkHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)){
            return ProductsViewModel(networkHelper, apiService) as T
        }
        throw IllegalArgumentException("Error")
    }
}