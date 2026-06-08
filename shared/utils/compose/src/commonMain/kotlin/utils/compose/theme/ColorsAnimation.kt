package utils.compose.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun animate(
    animationSpec: AnimationSpec<Color> = remember { spring(stiffness = Spring.StiffnessLow) },
    finishedListener: ((Color) -> Unit)? = null,
    color: () -> Color
): Color {
    return animateColorAsState(
        color(),
        animationSpec,
        label = "colorAnimation",
        finishedListener = finishedListener
    ).value
}

@Composable
fun ColorScheme.animated(
    animationSpec: AnimationSpec<Color> = remember { spring(stiffness = Spring.StiffnessLow) },
    finishedListener: ((Color) -> Unit)? = null
): ColorScheme {
    return this.copy(
        primary = animate(animationSpec, finishedListener = finishedListener) { this.primary },
        primaryContainer = animate(animationSpec) { this.primaryContainer },
        secondary = animate(animationSpec) { this.secondary },
        secondaryContainer = animate(animationSpec) { this.secondaryContainer },
        tertiary = animate(animationSpec) { this.tertiary },
        tertiaryContainer = animate(animationSpec) { this.tertiaryContainer },
        background = animate(animationSpec) { this.background },
        surface = animate(animationSpec) { this.surface },
        surfaceTint = animate(animationSpec) { this.surfaceTint },
        surfaceBright = animate(animationSpec) { this.surfaceBright },
        surfaceDim = animate(animationSpec) { this.surfaceDim },
        surfaceContainer = animate(animationSpec) { this.surfaceContainer },
        surfaceContainerHigh = animate(animationSpec) { this.surfaceContainerHigh },
        surfaceContainerHighest = animate(animationSpec) { this.surfaceContainerHighest },
        surfaceContainerLow = animate(animationSpec) { this.surfaceContainerLow },
        surfaceContainerLowest = animate(animationSpec) { this.surfaceContainerLowest },
        surfaceVariant = animate(animationSpec) { this.surfaceVariant },
        error = animate(animationSpec) { this.error },
        errorContainer = animate(animationSpec) { this.errorContainer },
        onPrimary = animate(animationSpec) { this.onPrimary },
        onPrimaryContainer = animate(animationSpec) { this.onPrimaryContainer },
        onSecondary = animate(animationSpec) { this.onSecondary },
        onSecondaryContainer = animate(animationSpec) { this.onSecondaryContainer },
        onTertiary = animate(animationSpec) { this.onTertiary },
        onTertiaryContainer = animate(animationSpec) { this.onTertiaryContainer },
        onBackground = animate(animationSpec) { this.onBackground },
        onSurface = animate(animationSpec) { this.onSurface },
        onSurfaceVariant = animate(animationSpec) { this.onSurfaceVariant },
        onError = animate(animationSpec) { this.onError },
        onErrorContainer = animate(animationSpec) { this.onErrorContainer },
        inversePrimary = animate(animationSpec) { this.inversePrimary },
        inverseSurface = animate(animationSpec) { this.inverseSurface },
        inverseOnSurface = animate(animationSpec) { this.inverseOnSurface },
        outline = animate(animationSpec) { this.outline },
        outlineVariant = animate(animationSpec) { this.outlineVariant },
        scrim = animate(animationSpec) { this.scrim },
    )
}