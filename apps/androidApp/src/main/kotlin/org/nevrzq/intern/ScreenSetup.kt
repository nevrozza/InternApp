package org.nevrzq.intern

import android.graphics.Color.TRANSPARENT
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

internal fun ComponentActivity.screenSetup() {
    edgeToEdgeSetup()
}

private fun ComponentActivity.edgeToEdgeSetup() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    enableEdgeToEdge(
        navigationBarStyle = SystemBarStyle.light(TRANSPARENT, TRANSPARENT)
    )
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}