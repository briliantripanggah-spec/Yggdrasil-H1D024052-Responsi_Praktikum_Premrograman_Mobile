package com.pemmob.yggdrasil.data.model

import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String?,
    @SerializedName("score") val score: Double?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("images") val images: AnimeImages?,
    @SerializedName("genres") val genres: List<Genre>?
)

data class AnimeImages(
    @SerializedName("jpg") val jpg: ImageUrl?
)

data class ImageUrl(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)