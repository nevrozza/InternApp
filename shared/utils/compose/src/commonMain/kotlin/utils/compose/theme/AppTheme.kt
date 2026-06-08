package utils.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val colors = if (isSystemInDarkTheme()) darkColorScheme
    else lightColorScheme

    MaterialExpressiveTheme(
        colorScheme = colors.animated(),
        motionScheme = MotionScheme.expressive()
    ) {
        content()
    }
}