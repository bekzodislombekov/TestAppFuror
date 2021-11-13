package com.android.example.myapplication.retrofit

import com.android.example.myapplication.models.RequestProductData
import com.android.example.myapplication.models.ProductData
import com.android.example.myapplication.models.ProductTypeData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/product")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int = 10
    ): List<ProductData>

    @DELETE("api/product/{id}")
    fun deleteProduct(
        @Path("id") id: Int
    ): Call<ResponseBody>

    @POST("api/product")
    fun addProduct(
        @Body productData: RequestProductData
    ): Call<ResponseBody>

    @PUT("api/product")
    fun editProduct(@Body productData: ProductData): Call<ResponseBody>

    @GET("api/product/get-product-types")
    suspend fun getProductTypes(): List<ProductTypeData>
}