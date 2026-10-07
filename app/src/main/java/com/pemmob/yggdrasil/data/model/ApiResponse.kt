package com.pemmob.yggdrasil.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data") val data: T
)