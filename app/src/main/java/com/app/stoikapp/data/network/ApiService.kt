package com.app.stoikapp.data.network

import com.app.stoikapp.data.model.ResponseGetToken
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("midtrans")
    fun getMidtrans() : Call<ResponseGetToken>
}