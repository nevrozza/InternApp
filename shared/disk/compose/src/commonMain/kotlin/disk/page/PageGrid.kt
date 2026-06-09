package disk.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import disk.models.resources.BinaryFileResource
import disk.models.resources.DirectoryResource
import disk.models.resources.DiskResource
import disk.models.resources.TextFileResource
import disk.page.PageConsts.BottomShadowHeight
import disk.page.PageConsts.GridHorizontalPadding
import disk.page.PageConsts.GridItemMinSize
import disk.page.PageConsts.GridItemSpacing
import disk.page.PageConsts.HeaderHeight
import utils.compose.consts.Paddings
import utils.compose.icons.file
import utils.compose.icons.folder
import utils.compose.icons.note


@Composable
internal fun PageGrid(
    topPadding: Dp,
    bottomPadding: Dp,
    authWidget: @Composable () -> Unit,
    items: List<DiskResource>
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(GridItemMinSize),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = GridHorizontalPadding,
            top = HeaderHeight + topPadding + Paddings.semiMedium,
            end = GridHorizontalPadding,
            bottom = BottomShadowHeight  + bottomPadding,
        ),
        horizontalArrangement = Arrangement.spacedBy(GridItemSpacing),
        verticalArrangement = Arrangement.spacedBy(GridItemSpacing),
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) },
            key = "AuthWidget"
        ) {
            Column {
                authWidget()
                Spacer(Modifier.height(Paddings.medium))
            }
        }

        items(
            items = items,
            key = DiskResource::localId,
        ) { resource ->
            DiskGridItem(resource = resource)
        }
    }
}

@Composable
private fun DiskGridItem(
    resource: DiskResource,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = Paddings.small).padding(top = Paddings.medium),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Paddings.small),
        ) {
            Icon(
                imageVector = when (resource) {
                    is DirectoryResource -> folder
                    is TextFileResource -> note
                    is BinaryFileResource -> file
                },
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = resource.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}