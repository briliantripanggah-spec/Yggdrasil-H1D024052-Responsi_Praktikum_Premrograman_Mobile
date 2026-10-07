package com.pemmob.yggdrasil.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LeafGreen,
    onPrimary = OnLeafGreen,
    primaryContainer = DeepGreenContainer,
    onPrimaryContainer = OnDeepGreenContainer,
    secondary = Gold,
    onSecondary = OnGold,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,
    background = ForestBackground,
    onBackground = OnForest,
    surface = ForestSurface,
    onSurface = OnForest,
    surfaceVariant = ForestSurfaceVariant,
    onSurfaceVariant = OnForestVariant,
    outline = ForestOutline
)

private val LightColorScheme = lightColorScheme(
    primary = TrunkGreen,
    onPrimary = OnTrunkGreen,
    primaryContainer = LightGreenContainer,
    onPrimaryContainer = OnLightGreenContainer,
    secondary = DarkGold,
    onSecondary = OnDarkGold,
    secondaryContainer = LightGoldContainer,
    onSecondaryContainer = OnLightGoldContainer,
    background = ParchmentBackground,
    onBackground = OnParchment,
    surface = ParchmentSurface,
    onSurface = OnParchment,
    surfaceVariant = ParchmentSurfaceVariant,
    onSurfaceVariant = OnParchmentVariant,
    outline = ParchmentOutline
)

@Composable
fun YggdrasilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}