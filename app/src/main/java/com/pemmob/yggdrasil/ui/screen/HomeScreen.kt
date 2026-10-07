package com.pemmob.yggdrasil.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.yggdrasil.R
import com.pemmob.yggdrasil.ui.components.AnimeCard
import com.pemmob.yggdrasil.ui.components.ErrorView
import com.pemmob.yggdrasil.ui.components.FeaturedAnimeCard
import com.pemmob.yggdrasil.ui.components.GenreFilterRow
import com.pemmob.yggdrasil.ui.components.LoadingView
import com.pemmob.yggdrasil.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onAnimeClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isSearching = state.query.isNotBlank()

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
            )

            TextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Hapus")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            GenreFilterRow(
                genres = state.genres,
                selectedGenreId = state.selectedGenreId,
                onGenreSelected = viewModel::onGenreSelected,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (state.isLoading && state.animeList.isNotEmpty()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading && state.animeList.isEmpty() -> LoadingView()

                    state.error != null -> ErrorView(
                        message = state.error!!,
                        onRetry = viewModel::retry
                    )

                    state.animeList.isEmpty() -> Text(
                        text = "Anime tidak ditemukan.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> {
                        val featured = state.animeList.first()
                        val rest = state.animeList.drop(1)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                FeaturedAnimeCard(
                                    anime = featured,
                                    badge = if (isSearching) "Hasil Teratas" else "Anime Unggulan",
                                    onClick = { onAnimeClick(featured.malId) }
                                )
                            }

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Column(modifier = Modifier.padding(top = 8.dp)) {
                                    Text(
                                        text = if (isSearching) "Hasil Pencarian" else "Anime Populer",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = if (isSearching) "Untuk \"${state.query}\"" else "Rating tertinggi di MyAnimeList",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            items(rest, key = { it.malId }) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    onClick = { onAnimeClick(anime.malId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
