package com.app.stoikapp.data.model


import com.google.gson.annotations.SerializedName

data class ResponseGetToken(
    @SerializedName("token")
    var token: String? = null
)