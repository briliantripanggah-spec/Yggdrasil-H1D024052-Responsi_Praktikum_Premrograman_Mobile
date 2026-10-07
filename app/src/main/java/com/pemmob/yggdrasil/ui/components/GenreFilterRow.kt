package com.pemmob.yggdrasil.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pemmob.yggdrasil.R
import com.pemmob.yggdrasil.data.model.Genre

@Composable
fun GenreFilterRow(
    genres: List<Genre>,
    selectedGenreId: Int?,
    onGenreSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            FilterChip(
                selected = selectedGenreId == null,
                onClick = { onGenreSelected(null) },
                label = { Text(stringResource(R.string.all_genres)) }
            )
        }

        items(genres, key = { it.malId }) { genre ->
            FilterChip(
                selected = selectedGenreId == genre.malId,
                onClick = { onGenreSelected(genre.malId) },
                label = { Text(genre.name) }
            )
        }
    }
}
