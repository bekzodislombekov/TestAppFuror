package com.android.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class RequestProductData(
    val address: String,
    val cost: Int,
    @SerializedName("created_date")
    val createdDate: String,
    val name_uz: String,
    @SerializedName("product_type_id")
    val productTypeId: Int
)