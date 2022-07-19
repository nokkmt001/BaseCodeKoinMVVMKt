package com.phat.testbase.dev.extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.phat.testbase.R
import kotlinx.coroutines.CoroutineScope

/** get a material container arc transform. */
internal fun getContentTransform(context: Context): MaterialContainerTransform {
  return MaterialContainerTransform().apply {
    addTarget(android.R.id.content)
    duration = 450
    pathMotion = MaterialArcMotion()
    isElevationShadowEnabled = true
    startElevation = 9f
    endElevation = 9f
    startContainerColor = ContextCompat.getColor(context, R.color.colorPrimary)
  }
}

fun Activity.launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
  launchWhenCreated { block.invoke(this) }
}

fun Activity.initLaunch(vararg listBlock: suspend CoroutineScope.() -> Unit) {
  listBlock.forEach {
    launchWhenCreated { it.invoke(this) }
  }
}

fun Activity.lockscreen(isLock: Boolean){
  if (isLock) {
    window?.setFlags(
      WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
      WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
  } else {
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
  }
}

fun Activity.toast(text: String) {
  Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(@androidx.annotation.StringRes res: Int) {
  Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
}

fun Activity.toastLong(text: String) {
  Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Activity.toastLong(@androidx.annotation.StringRes res: Int) {
  Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}

fun Activity.wait(segs: Int, closure: () -> Unit) {
  if (Looper.myLooper() == Looper.getMainLooper()) {
    Handler().postDelayed(closure, segs.toLong() * 1000)
  } else {
    Thread.sleep(segs.toLong() * 1000)
    closure()
  }
}
fun Activity.hideKeyboard(){
  val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}


fun Activity.isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/** apply material exit container transformation. */
fun AppCompatActivity.applyExitMaterialTransform() {
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementsUseOverlay = false
}

/** apply material entered container transformation. */
fun AppCompatActivity.applyMaterialTransform(transitionName: String?) {
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  ViewCompat.setTransitionName(findViewById(android.R.id.content), transitionName)

  // set up shared element transition
  setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementEnterTransition = getContentTransform(this)
  window.sharedElementReturnTransition = getContentTransform(this)
}
