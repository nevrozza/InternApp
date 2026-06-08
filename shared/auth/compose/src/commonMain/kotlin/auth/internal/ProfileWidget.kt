package auth.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import auth.models.YandexUserProfile
import utils.compose.consts.Paddings
import utils.compose.icons.logout
import utils.compose.widgets.CoilImage

@Composable
internal fun ProfileWidget(
    height: Dp = 50.dp,
    profile: YandexUserProfile?,
    onLogoutClick: () -> Unit
) {

    val usernameSpacer: @Composable RowScope.(padding: Dp) -> Unit =
        { padding -> Spacer(Modifier.width(padding).weight(0.1f, fill = false)) }

    profile?.let {
        Row(
            Modifier.height(IntrinsicSize.Max).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(height)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            brush = Brush.sweepGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.error,
                                    MaterialTheme.colorScheme.onSurface,
                                )
                            ),
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .background(
                            MaterialTheme.colorScheme.surface
                        )
                ) {
                    CoilImage(
                        model = profile.avatarUrl,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                usernameSpacer(Paddings.semiMedium)
                Text(
                    profile.displayName,
                    modifier = Modifier.weight(1f, fill = false),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium
                )
                usernameSpacer(Paddings.ultraUltraSmall)
            }
            IconButton(
                onClick = onLogoutClick,
                modifier = Modifier.size(height)
            ) {
                Icon(logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
            }

        }
    } ?: LoadingIndicator()
}
