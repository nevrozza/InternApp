package utils.compose.icons


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val file: ImageVector
    get() {
        if (_file != null) {
            return _file!!
        }
        _file =
            ImageVector.Builder(
                name = "file",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(7f, 22f)
                        quadTo(6.18f, 22f, 5.59f, 21.41f)
                        reflectiveQuadTo(5f, 20f)
                        verticalLineTo(4f)
                        quadTo(5f, 3.17f, 5.59f, 2.59f)
                        reflectiveQuadTo(7f, 2f)
                        horizontalLineToRelative(6.18f)
                        quadToRelative(0.4f, 0f, 0.76f, 0.15f)
                        reflectiveQuadToRelative(0.64f, 0.43f)
                        lineToRelative(3.85f, 3.85f)
                        quadTo(18.7f, 6.7f, 18.85f, 7.06f)
                        reflectiveQuadTo(19f, 7.82f)
                        verticalLineTo(20f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(17f, 22f)
                        horizontalLineTo(7f)
                        close()
                        moveTo(11f, 4f)
                        horizontalLineTo(7f)
                        verticalLineTo(20f)
                        horizontalLineTo(17f)
                        verticalLineTo(10f)
                        horizontalLineTo(14f)
                        quadTo(12.75f, 10f, 11.88f, 9.13f)
                        reflectiveQuadTo(11f, 7f)
                        verticalLineTo(4f)
                        close()
                        moveToRelative(2f, 0f)
                        verticalLineTo(7f)
                        quadToRelative(0f, 0.43f, 0.29f, 0.71f)
                        reflectiveQuadTo(14f, 8f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(7.82f)
                        lineTo(13.18f, 4f)
                        horizontalLineTo(13f)
                        close()
                        moveTo(10f, 19f)
                        quadTo(9.58f, 19f, 9.29f, 18.71f)
                        quadTo(9f, 18.43f, 9f, 18f)
                        reflectiveQuadTo(9.29f, 17.29f)
                        quadTo(9.58f, 17f, 10f, 17f)
                        horizontalLineToRelative(2f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(13f, 18f)
                        reflectiveQuadToRelative(-0.29f, 0.71f)
                        reflectiveQuadTo(12f, 19f)
                        horizontalLineTo(10f)
                        close()
                        moveToRelative(0f, -4f)
                        quadTo(9.58f, 15f, 9.29f, 14.71f)
                        reflectiveQuadTo(9f, 14f)
                        reflectiveQuadTo(9.29f, 13.29f)
                        quadTo(9.58f, 13f, 10f, 13f)
                        horizontalLineToRelative(4f)
                        quadToRelative(0.43f, 0f, 0.71f, 0.29f)
                        reflectiveQuadTo(15f, 14f)
                        reflectiveQuadToRelative(-0.29f, 0.71f)
                        reflectiveQuadTo(14f, 15f)
                        horizontalLineTo(10f)
                        close()
                    }
                }
                .build()
        return _file!!
    }

private var _file: ImageVector? = null
