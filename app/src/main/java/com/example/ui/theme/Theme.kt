package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val KaramLightColorScheme = lightColorScheme(
    primary = SkyBluePrimary,
    onPrimary = Color.White,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = SkyBlueDark,
    secondary = CardPeriwinkle,
    onSecondary = Color.White,
    secondaryContainer = BadgePurpleBg,
    onSecondaryContainer = CardPeriwinkleAccent,
    tertiary = CardWarmAmber,
    onTertiary = Color.White,
    tertiaryContainer = BadgeAmberBg,
    onTertiaryContainer = CardWarmAmberAccent,
    background = SoftIceBackground,
    onBackground = InkDark,
    surface = PureWhiteSurface,
    onSurface = InkDark,
    surfaceVariant = Color(0xFFE8F2FC),
    onSurfaceVariant = InkMedium,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun KaramTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = KaramLightColorScheme,
            typography = Typography,
            content = content
        )
    }
}
