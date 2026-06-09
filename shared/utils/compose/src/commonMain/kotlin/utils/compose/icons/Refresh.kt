package utils.compose.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val refresh: ImageVector
    get() {
        if (_refresh != null) {
            return _refresh!!
        }
        _refresh = ImageVector.Builder(
            name = "refresh",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                stroke = null,
                fillAlpha = 1f,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(17.65f, 6.35f)
                curveTo(16.2f, 4.9f, 14.21f, 4f, 12f, 4f)
                curveTo(7.58f, 4f, 4.01f, 7.58f, 4.01f, 12f)
                curveTo(4.01f, 16.42f, 7.58f, 20f, 12f, 20f)
                curveTo(15.73f, 20f, 18.84f, 17.45f, 19.73f, 14f)
                horizontalLineTo(17.65f)
                curveTo(16.82f, 16.33f, 14.6f, 18f, 12f, 18f)
                curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
                curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
                curveTo(13.66f, 6f, 15.14f, 6.69f, 16.22f, 7.78f)
                lineTo(13f, 11f)
                horizontalLineTo(20f)
                verticalLineTo(4f)
                lineTo(17.65f, 6.35f)
                close()
            }
        }.build()
        return _refresh!!
    }

private var _refresh: ImageVector? = null