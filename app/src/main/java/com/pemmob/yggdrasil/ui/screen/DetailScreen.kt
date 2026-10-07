package com.pemmob.yggdrasil.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pemmob.yggdrasil.ui.components.ErrorView
import com.pemmob.yggdrasil.ui.components.LoadingView
import com.pemmob.yggdrasil.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        val anime = state.anime
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> LoadingView(Modifier.padding(innerPadding))

                state.error != null -> ErrorView(
                    message = state.error!!,
                    onRetry = viewModel::loadDetail,
                    modifier = Modifier.padding(innerPadding)
                )

                anime != null -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(380.dp)) {
                        AsyncImage(
                            model = anime.images?.jpg?.largeImageUrl ?: anime.images?.jpg?.imageUrl,
                            contentDescription = anime.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background
                                        ),
                                        startY = 400f
                                    )
                                )
                        )
                        Text(
                            text = anime.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            StatCard(
                                label = "Rating",
                                value = "★ ${anime.score?.toString() ?: "N/A"}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Episode",
                                value = anime.episodes?.toString() ?: "?",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Status",
                                value = anime.status ?: "-",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "Genre",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            anime.genres.orEmpty().forEach { genre ->
                                AssistChip(onClick = {}, label = { Text(genre.name) })
                            }
                        }

                        Text(
                            text = "Sinopsis",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = anime.synopsis ?: "Sinopsis belum tersedia.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0x99000000),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
