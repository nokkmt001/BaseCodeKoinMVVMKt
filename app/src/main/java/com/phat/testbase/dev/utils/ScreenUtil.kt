package com.phat.testbase.dev.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat

object ScreenUtil {

    const val MAX_BRIGHTNESS = 1F

    @JvmStatic
    fun lockScreen(activity: Activity?, isLock: Boolean) {
        if (isLock) {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    @JvmStatic
    fun setStatusBarColor(activity: Activity?, color: Int? = null, colorRes: Int? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.let { window ->
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                val c = color ?: colorRes?.let { ContextCompat.getColor(activity, colorRes) }
                ?: Color.WHITE
                if (c == Color.WHITE) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                window.statusBarColor = c
            }
        }
    }

    fun makeStatusBarTransparent(activity: Activity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        systemUiVisibility =
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                    decorView.systemUiVisibility = systemUiVisibility
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    fun setScreenBright(activity: Activity?, bright: Float) {
        activity?.let {
            val attributes = it.window.attributes
            attributes.screenBrightness = bright
            it.window.attributes = attributes
        }
    }
}