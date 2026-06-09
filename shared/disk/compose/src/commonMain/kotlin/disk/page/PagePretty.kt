package disk.page

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import disk.page.PageConsts.HeaderHeight
import utils.compose.consts.Paddings

@Composable
internal fun PageHeader(
    path: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HeaderHeight)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Paddings.medium),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = path,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
internal fun TopShadow(
    modifier: Modifier = Modifier,
    height: Dp,
    paddingHeight: Dp,
) {
    val backgroundColor = MaterialTheme.colorScheme.background

    Column {
        Box(Modifier.fillMaxWidth().height(paddingHeight).background(backgroundColor))
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            Color.Transparent,
                        )
                    )
                )
        )
    }
}

@Composable
internal fun BottomShadow(
    modifier: Modifier = Modifier,
    height: Dp,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background,
                    )
                )
            )
    )
}