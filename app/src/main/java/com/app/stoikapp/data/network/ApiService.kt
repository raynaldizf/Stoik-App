package com.app.stoikapp.data.network

import com.app.stoikapp.data.model.ResponseGetToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("generate-token")
    fun getMidtrans(
        @Query("customer_first_name") customer_first_name: String,
        @Query("customer_phone") customer_phone: String,
        @Query("customer_email") customer_email: String,
        @Query("product_id") product_id: String,
        @Query("product_name") product_name: String,
        @Query("quantity") quantity: Int,
        @Query("price") price: Int
    ): Call<ResponseGetToken>
}