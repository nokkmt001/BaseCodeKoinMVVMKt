package com.phat.testbase.view.ui.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.phat.testbase.extensions.getFirstGenericParameter
import com.skydoves.bindables.BindingActivity
import org.greenrobot.eventbus.EventBus

abstract class BaseMvvmActivity<T : ViewDataBinding, VM : BaseMvvmViewModel>(@LayoutRes private val contentLayoutId: Int) : BindingActivity<T>(contentLayoutId) {
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        startFlow()

        viewModel = ViewModelProviders.of(this)
            .get(getFirstGenericParameter<VM>())
        binding {
            initView()
        }
        initData()
        initVM()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    open fun startFlow(){}

    open fun initView(){}

    open fun initData(){}

    open fun initVM(){}

    open fun useEventBus(): Boolean = false

    /**
     * goto @baseFragment
     */
    var isPushFragment = false
    open fun pushFragment(baseFragment: Fragment) {
        isPushFragment = true
    }

    /**
     * remove fragment in back stack
     */
    open fun popFragment() {
        isPushFragment = false
    }

    /**
     * goto @baseFragment fragment in back stack (it will remove back fragment in back stack)
     * ex: backstack[0,1,2,3] -> popToFragment(1) --> backstack[0,1] (fragment 2,3 will be removed)
     */
    open fun popToFragment(baseFragment: Fragment) {
        isPushFragment = false
    }

    /**
     * remove all fragment in back stack (except main fragments)
     */
    open fun popToRootFragment() {
        isPushFragment = false
    }

    /**
     * Clear focus on touch outside for all EditText inputs.
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm!!.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}