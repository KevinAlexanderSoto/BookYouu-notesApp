package com.kalex.bookyouu_notesapp.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Core Investment-inspired Theme Colors
val PrimaryDarkGreen = Color(0xFF004D40)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFE0F2F1)
val OnPrimaryContainer = Color(0xFF004D40)

val SecondaryGreen = Color(0xFF2D5D57)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFD3E8E1)
val OnSecondaryContainer = Color(0xFF07201A)

val TertiaryTeal = Color(0xFF00695C)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFB2DFDB)
val OnTertiaryContainer = Color(0xFF00201C)

val NeutralGrey = Color(0xFF5A5F5E)
val NeutralVariant = Color(0xFFE0E5E3)

val LightColorScheme = lightColorScheme(
    primary = PrimaryDarkGreen,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryGreen,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryTeal,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Color(0xFFF7F9F8),
    onBackground = Color(0xFF191C1B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191C1B),
    surfaceVariant = NeutralVariant,
    onSurfaceVariant = Color(0xFF455A64),
    outline = Color(0xFF717977),
    outlineVariant = Color(0xFFC4CDC9),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DB6AC),
    onPrimary = Color(0xFF003730),
    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = Color(0xFFE0F2F1),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF00332C),
    secondaryContainer = Color(0xFF2D5D57),
    onSecondaryContainer = Color(0xFFD3E8E1),
    tertiary = Color(0xFF26A69A),
    onTertiary = Color(0xFF003831),
    tertiaryContainer = Color(0xFF00695C),
    onTertiaryContainer = Color(0xFFB2DFDB),
    background = Color(0xFF121514),
    onBackground = Color(0xFFE0E3E1),
    surface = Color(0xFF191C1B),
    onSurface = Color(0xFFE0E3E1),
    surfaceVariant = Color(0xFF2B3331),
    onSurfaceVariant = Color(0xFFBFC9C6),
    outline = Color(0xFF899390),
    outlineVariant = Color(0xFF3F4947),
)
