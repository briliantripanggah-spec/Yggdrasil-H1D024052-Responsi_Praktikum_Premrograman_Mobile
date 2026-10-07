package com.pemmob.yggdrasil.data.repository

import com.pemmob.yggdrasil.data.model.Anime
import com.pemmob.yggdrasil.data.model.Genre
import com.pemmob.yggdrasil.data.remote.RetrofitInstance
import com.pemmob.yggdrasil.data.remote.TenraiApi

class AnimeRepository(
    private val api: TenraiApi = RetrofitInstance.api
) {

    suspend fun searchAnime(
        query: String,
        genreId: Int? = null
    ): Result<List<Anime>> = runCatching {
        val trimmed = query.trim()
        api.searchAnime(
            query = trimmed.ifEmpty { null },
            genres = genreId?.toString(),
            orderBy = if (trimmed.isEmpty()) "score" else null,
            sort = if (trimmed.isEmpty()) "desc" else null
        ).data
    }

    suspend fun getAnimeDetail(id: Int): Result<Anime> = runCatching {
        api.getAnimeById(id).data
    }

    suspend fun getGenres(): Result<List<Genre>> = runCatching {
        api.getGenres().data.sortedBy { it.name }
    }
}
