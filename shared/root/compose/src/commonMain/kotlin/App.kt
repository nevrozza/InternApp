import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import root.RootComponent
import root.RootScreen
import utils.compose.theme.AppTheme


@Composable
fun App(component: RootComponent) {
    AppTheme {
        Surface {
            RootScreen(component)
        }
    }
}