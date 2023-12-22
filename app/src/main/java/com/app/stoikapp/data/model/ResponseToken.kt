package com.app.stoikapp.data.model


import com.google.gson.annotations.SerializedName

data class ResponseToken(
    @SerializedName("token")
    var token: String?
)