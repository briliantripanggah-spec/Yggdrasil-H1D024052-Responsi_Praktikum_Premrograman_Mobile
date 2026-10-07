package com.pemmob.yggdrasil.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("name") val name: String
)