package com.phat.testbase.devphat.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.phat.testbase.devphat.extensions.lifecycle.ResultLifecycle
import com.phat.testbase.devphat.xbase.BaseMvvmActivity
import com.phat.testbase.devphat.xbase.BaseMvvmFragment
import kotlin.reflect.KClass

interface Dispatcher {
    fun getResultLifecycle(): ResultLifecycle {
        return when (this) {
            is BaseMvvmActivity<*, *> -> resultLife
            is BaseMvvmFragment<*, *> -> resultLife
            else -> throw UnsupportedOperationException("Not support for ${this.javaClass.name}")
        }
    }
}
const val REQUEST_FOR_RESULT_INSTANTLY = 1000

inline fun <reified T : AppCompatActivity> Dispatcher.open(vararg args: Pair<String, Any>): Dispatcher {
    start(T::class) { put(*args) }
    return this
}

inline fun <reified T : AppCompatActivity> Dispatcher.openRequest(requestId: Int, vararg args: Pair<String, Any>): Dispatcher {
    startForResult(requestId, T::class) { put(*args) }
    return this
}

inline fun <reified T : AppCompatActivity> Dispatcher.openWithIntent(function: Intent.() -> Unit): Dispatcher {
    start(T::class, function)
    return this
}

inline fun <reified T : Activity> Dispatcher.openForResult(vararg args: Pair<String, Any>): ResultLifecycle {
    return openClassForResult(T::class, *args)
}

fun Dispatcher.openClassForResult(clazz: KClass<out Activity>, vararg args: Pair<String, Any>): ResultLifecycle {
    startForResult(REQUEST_FOR_RESULT_INSTANTLY, clazz) { put(*args) }
    return getResultLifecycle()
}

fun Dispatcher.openAppInGooglePlay(packageName: String) {
    try {
        start(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (abe: android.content.ActivityNotFoundException) {
        start(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }
}

private fun Dispatcher.start(intent: Intent) {
    when (this) {
        is BaseMvvmFragment<*, *> -> startActivity(intent)
        is BaseMvvmActivity<*, *> -> startActivity(intent)
        else -> throw IllegalArgumentException("This is not instance of BaseActivity or BaseFragment")
    }
}

inline fun Dispatcher.start(clazz: KClass<out Activity>, function: Intent.() -> Unit) {
    when (this) {
        is BaseMvvmFragment<*, *> -> startActivity(Intent(activity!!, clazz.java).apply(function))
        is BaseMvvmActivity<*, *> -> startActivity(Intent(this, clazz.java).apply(function))
        else -> throw IllegalArgumentException("This is not instance of BaseActivity or BaseFragment")
    }
}

fun Dispatcher.startForResult(requestId: Int, clazz: KClass<out Activity>, function: Intent.() -> Unit) {
    when (this) {
        is BaseMvvmFragment<*, *> -> startActivityForResult(Intent(activity!!, clazz.java).apply(function), requestId)
        is BaseMvvmActivity<*, *> -> startActivityForResult(Intent(this, clazz.java).apply(function), requestId)
        else -> throw IllegalArgumentException("This is not instance of BaseActivity or BaseFragment")
    }
}

fun Dispatcher.close(result: Int, vararg args: Pair<String, Any>): Dispatcher {
    val intent = Intent().also { it.put(*args) }
    when (this) {
        is androidx.fragment.app.Fragment -> activity!!.close(result, intent)
        is Activity -> close(result, intent)
    }
    return this
}

fun Dispatcher.closeSuccess(vararg args: Pair<String, Any>): Dispatcher {
    close(Activity.RESULT_OK, *args)
    return this
}

fun Dispatcher.closeCancel(vararg args: Pair<String, Any>): Dispatcher {
    close(Activity.RESULT_CANCELED, *args)
    return this
}

fun Activity.close(result: Int, intent: Intent) {
    setResult(result, intent)
    finish()
}

fun Dispatcher.close(): Dispatcher {
    when (this) {
        is androidx.fragment.app.Fragment -> activity!!.finish()
        is Activity -> finish()
    }
    return this
}