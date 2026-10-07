package com.pemmob.yggdrasil.data.remote

import com.pemmob.yggdrasil.data.model.Anime
import com.pemmob.yggdrasil.data.model.ApiResponse
import com.pemmob.yggdrasil.data.model.Genre
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TenraiApi {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("genres") genres: String? = null,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null,
        @Query("limit") limit: Int = 24,
        @Query("sfw") sfw: Boolean = true
    ): ApiResponse<List<Anime>>

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): ApiResponse<Anime>

    @GET("genres/anime")
    suspend fun getGenres(): ApiResponse<List<Genre>>
}
