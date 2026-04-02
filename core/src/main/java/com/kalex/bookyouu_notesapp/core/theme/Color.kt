package com.kalex.bookyouu_notesapp.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Core colors
val PrimaryTeal = Color(0xFF006A6A)
val OnPrimaryTeal = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFCCE8E8)
val OnPrimaryContainer = Color(0xFF002020)

val SecondaryGreen = Color(0xFF4B635C)
val OnSecondaryGreen = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFD3E8E1)
val OnSecondaryContainer = Color(0xFF07201A)

val TertiaryForest = Color(0xFF1B6C31)
val OnTertiaryForest = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFB3F5BD)
val OnTertiaryContainer = Color(0xFF002108)

val NeutralGrey = Color(0xFF5A5F5E)
val NeutralVariant = Color(0xFFDBE5E0)

val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = OnPrimaryTeal,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryGreen,
    onSecondary = OnSecondaryGreen,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryForest,
    onTertiary = OnTertiaryForest,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Color(0xFFF4FAF9),
    onBackground = Color(0xFF191C1B),
    surface = Color(0xFFF4FAF9),
    onSurface = Color(0xFF191C1B),
    surfaceVariant = NeutralVariant,
    onSurfaceVariant = Color(0xFF3F4947),
    outline = Color(0xFF6F7977),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DDADA),
    onPrimary = Color(0xFF003737),
    primaryContainer = Color(0xFF004F4F),
    onPrimaryContainer = Color(0xFFCCE8E8),
    secondary = Color(0xFFB2CCC5),
    onSecondary = Color(0xFF1D352F),
    secondaryContainer = Color(0xFF334B45),
    onSecondaryContainer = Color(0xFFD3E8E1),
    tertiary = Color(0xFF98D8A2),
    onTertiary = Color(0xFF003916),
    tertiaryContainer = Color(0xFF005322),
    onTertiaryContainer = Color(0xFFB3F5BD),
    background = Color(0xFF191C1B),
    onBackground = Color(0xFFE0E3E1),
    surface = Color(0xFF191C1B),
    onSurface = Color(0xFFE0E3E1),
    surfaceVariant = Color(0xFF3F4947),
    onSurfaceVariant = Color(0xFFBFC9C6),
    outline = Color(0xFF899390),
)
