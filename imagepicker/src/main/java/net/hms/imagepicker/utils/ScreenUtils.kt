package net.hms.imagepicker.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object ScreenUtils {

    fun getScreenSize(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val out = Point()
        display.getSize(out)
        return out
    }
}
