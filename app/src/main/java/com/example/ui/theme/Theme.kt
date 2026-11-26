package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = ObsidianVoid,
    primaryContainer = GoldMuted,
    onPrimaryContainer = GoldBright,
    secondary = GoldBright,
    onSecondary = ObsidianVoid,
    secondaryContainer = ObsidianElevated,
    onSecondaryContainer = IvoryText,
    tertiary = EmeraldAccent,
    onTertiary = ObsidianVoid,
    background = ObsidianVoid,
    onBackground = IvoryText,
    surface = ObsidianSurface,
    onSurface = IvoryText,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = MutedPlatinum,
    surfaceContainerLowest = ObsidianVoid,
    surfaceContainerLow = ObsidianSurface,
    surfaceContainer = ObsidianCard,
    surfaceContainerHigh = ObsidianElevated,
    outline = GoldDeep,
    outlineVariant = SubtleSlate,
    error = RubyError,
    onError = ObsidianVoid,
    errorContainer = RubySurface,
    onErrorContainer = RubyError,
)

private val LuxuryShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun AngkaBandarTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography = AngkaBandarTypography,
        shapes = LuxuryShapes,
        content = content,
    )
}
