package com.android.example.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.myapplication.retrofit.ApiService

class ViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)){
            return ProductsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Error")
    }
}