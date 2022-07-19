package com.phat.testbase.devphat.extensions

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.*
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.phat.testbase.model.RepoState
import com.phat.testbase.model.UiState
import kotlinx.coroutines.CoroutineScope
import java.io.Serializable

fun Fragment.launchWhenCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenCreated { block.invoke(this) }
}

fun <T> handleStateFlow(state: UiState<T>, onSuccess: (() -> Unit)? = null, onFail: (() -> Unit)? = null) {
    when (state.state) {
        RepoState.SUCCESS -> onSuccess?.invoke()
        RepoState.FAIL -> onFail?.invoke()
        else -> {

        }
    }
}

private fun Fragment.isParentVisible(): Boolean {
    var parent = parentFragment
    while (parent != null) {
        if (parent.isHidden) return false
        parent = parent.parentFragment
    }
    return true
}

fun Fragment.isVisibleInParent() = !isHidden && userVisibleHint

fun Fragment.isVisibleOnScreen() = isVisibleInParent() && isParentVisible()

fun Fragment.dispatchHidden(hidden: Boolean) = findChildVisible()?.onHiddenChanged(hidden)

fun Fragment.findChildVisible(): Fragment? {
    var childVisible = childFragmentManager.primaryNavigationFragment
    if (childVisible == null) {
        childVisible = childFragmentManager.fragments.find { it.isVisibleInParent() }
    }
    return childVisible
}

inline fun <reified T: Parcelable> Fragment.addParcelArgs(objects: T): Fragment {
    val args = Bundle()
    args.putParcelable(T::class.java.name, objects)
    arguments = args
    return this
}

inline fun <reified T: ArrayList<String>> Fragment.addArrayListStringArgs(objects: T): Fragment {
    val args = Bundle()
    args.putStringArrayList("StringArrayList", objects)
    arguments = args
    return this
}

fun Fragment.addArgsT(newArgs: Bundle) : Fragment {
    var args = arguments
    if (args == null) args = Bundle()
    args.putAll(newArgs)
    arguments = args
    return this
}

fun Fragment.addArgs(newArgs: Bundle) {
    var args = arguments
    if (args == null) args = Bundle()
    args.putAll(newArgs)
    arguments = args
}

fun Fragment.addArgs(vararg newArgs: Pair<String, Serializable>) {
    var args = arguments
    if (args == null) args = Bundle()
    newArgs.forEach { args.putSerializable(it.first, it.second) }
    arguments = args
}

fun Fragment.initLaunch(vararg listBlock: suspend CoroutineScope.() -> Unit) {
    listBlock.forEach {
        launchWhenCreated { it.invoke(this) }
    }
}

inline fun TextView.doBeforeTextChanged(
        crossinline action: (
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
        ) -> Unit
) = addTextChangedListener(beforeTextChanged = action)


inline fun TextView.doOnTextChanged(
        crossinline action: (
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
        ) -> Unit
) = addTextChangedListener(onTextChanged = action)

inline fun TextView.doAfterTextChanged(
        crossinline action: (text: Editable?) -> Unit
) = addTextChangedListener(afterTextChanged = action)

inline fun TextView.addTextChangedListener(
        crossinline beforeTextChanged: (
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
        ) -> Unit = { _, _, _, _ -> },
        crossinline onTextChanged: (
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
        ) -> Unit = { _, _, _, _ -> },
        crossinline afterTextChanged: (text: Editable?) -> Unit = {}
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s)
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged.invoke(text, start, count, after)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(text, start, before, count)
        }
    }
    addTextChangedListener(textWatcher)

    return textWatcher
}

inline fun getValueAnimator(
        forward: Boolean = true,
        duration: Long,
        interpolator: TimeInterpolator,
        crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a =
            if (forward) ValueAnimator.ofFloat(0f, 1f)
            else ValueAnimator.ofFloat(1f, 0f)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}

fun View.isVisible(boolean: Boolean) {
    visibility = if (boolean) View.VISIBLE else View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visibleView() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun bundleEvent(typeHotDeal: Int? = null,
                idCondition: String? = null,
                typeProduct: Int? = null,
                eventId: Int? = null,
                imageHotDeal: String? = null,
                canBuy: Boolean = true): Bundle {
    val bundle = Bundle()
   /* bundle.putInt(DATA_BUNDLE.BUNDLE_EVENT_ID_EVENT, eventId ?: -1)
    bundle.putString(DATA_BUNDLE.BUNDLE_EVENT_ID_CONDITION, idCondition)
    bundle.putInt(DATA_BUNDLE.BUNDLE_EVENT_TYPE_PRODUCT, typeProduct ?: -1)
    bundle.putString(DATA_BUNDLE.BUNDLE_EVENT_IMAGE_HOT_DEAL, imageHotDeal)
    bundle.putInt(DATA_BUNDLE.BUNDLE_EVENT_TYPE_HOT_DEAL, typeHotDeal ?: -1)
    bundle.putBoolean(DATA_BUNDLE.BUNDLE_EVENT_CAN_BUY, canBuy)*/
    return bundle
}

fun format2Fonts(text1: String, text2: String, context: Context): SpannableStringBuilder {
    val firstFont =
            Typeface.createFromAsset(context.assets, "font/nunito_sans_semi_bold.ttf")
    val secondFont =
            Typeface.createFromAsset(context.assets, "font/nunito_sans_bold.ttf")
    val stringBuilder = SpannableStringBuilder(text1 + text2)
    stringBuilder.setSpan(
            CustomTypefaceSpan("", firstFont),
            0,
            text1.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
    )
    stringBuilder.setSpan(
            CustomTypefaceSpan("", secondFont),
            text1.length,
            text1.length + text2.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
    )
    return stringBuilder
}

class CustomTypefaceSpan(family: String?, private val newType: Typeface) : TypefaceSpan(family) {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = old?.style ?: 0
        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = tf
    }

}