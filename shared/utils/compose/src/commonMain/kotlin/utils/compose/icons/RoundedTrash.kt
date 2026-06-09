package utils.compose.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val rounded_trash: ImageVector
    get() {
        if (_roundedTrash != null) {
            return _roundedTrash!!
        }
        _roundedTrash = ImageVector.Builder(
            name = "rounded_trash",
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
                moveTo(7f, 21f)
                curveTo(6.45f, 21f, 5.98f, 20.8f, 5.59f, 20.41f)
                curveTo(5.2f, 20.02f, 5f, 19.55f, 5f, 19f)
                verticalLineTo(8f)
                curveTo(4.72f, 8f, 4.48f, 7.9f, 4.29f, 7.71f)
                curveTo(4.1f, 7.52f, 4f, 7.28f, 4f, 7f)
                reflectiveCurveTo(4.1f, 6.48f, 4.29f, 6.29f)
                curveTo(4.48f, 6.1f, 4.72f, 6f, 5f, 6f)
                horizontalLineTo(9f)
                verticalLineTo(5f)
                curveTo(9f, 4.72f, 9.1f, 4.48f, 9.29f, 4.29f)
                curveTo(9.48f, 4.1f, 9.72f, 4f, 10f, 4f)
                horizontalLineTo(14f)
                curveTo(14.28f, 4f, 14.52f, 4.1f, 14.71f, 4.29f)
                curveTo(14.9f, 4.48f, 15f, 4.72f, 15f, 5f)
                verticalLineTo(6f)
                horizontalLineTo(19f)
                curveTo(19.28f, 6f, 19.52f, 6.1f, 19.71f, 6.29f)
                curveTo(19.9f, 6.48f, 20f, 6.72f, 20f, 7f)
                reflectiveCurveTo(19.9f, 7.52f, 19.71f, 7.71f)
                curveTo(19.52f, 7.9f, 19.28f, 8f, 19f, 8f)
                verticalLineTo(19f)
                curveTo(19f, 19.55f, 18.8f, 20.02f, 18.41f, 20.41f)
                curveTo(18.02f, 20.8f, 17.55f, 21f, 17f, 21f)
                horizontalLineTo(7f)
                close()
                moveTo(9f, 17f)
                curveTo(9f, 17.28f, 9.1f, 17.52f, 9.29f, 17.71f)
                reflectiveCurveTo(9.72f, 18f, 10f, 18f)
                reflectiveCurveTo(10.52f, 17.9f, 10.71f, 17.71f)
                reflectiveCurveTo(11f, 17.28f, 11f, 17f)
                verticalLineTo(10f)
                curveTo(11f, 9.72f, 10.9f, 9.48f, 10.71f, 9.29f)
                reflectiveCurveTo(10.28f, 9f, 10f, 9f)
                reflectiveCurveTo(9.48f, 9.1f, 9.29f, 9.29f)
                reflectiveCurveTo(9f, 9.72f, 9f, 10f)
                verticalLineTo(17f)
                close()
                moveTo(13f, 17f)
                curveTo(13f, 17.28f, 13.1f, 17.52f, 13.29f, 17.71f)
                reflectiveCurveTo(13.72f, 18f, 14f, 18f)
                reflectiveCurveTo(14.52f, 17.9f, 14.71f, 17.71f)
                reflectiveCurveTo(15f, 17.28f, 15f, 17f)
                verticalLineTo(10f)
                curveTo(15f, 9.72f, 14.9f, 9.48f, 14.71f, 9.29f)
                reflectiveCurveTo(14.28f, 9f, 14f, 9f)
                reflectiveCurveTo(13.48f, 9.1f, 13.29f, 9.29f)
                reflectiveCurveTo(13f, 9.72f, 13f, 10f)
                verticalLineTo(17f)
                close()
            }
        }.build()
        return _roundedTrash!!
    }

private var _roundedTrash: ImageVector? = null
