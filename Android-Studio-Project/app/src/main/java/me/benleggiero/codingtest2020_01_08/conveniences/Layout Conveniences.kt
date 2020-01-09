package me.benleggiero.codingtest2020_01_08.conveniences

import android.app.Activity
import android.util.DisplayMetrics
import java.lang.Integer.max



val Activity.currentMaterialColumnCount: Int get() =
    materialColumnCount(resources.displayMetrics.widthDp)


private fun materialColumnCount(widthDp: Float): Int {
    return max(1, (widthDp / 90).toInt())
}


val DisplayMetrics.widthDp get() = widthPixels / density

