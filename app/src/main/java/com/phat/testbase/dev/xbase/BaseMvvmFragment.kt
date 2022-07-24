package com.phat.testbase.dev.xbase

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phat.testbase.dev.event.NetworkChangeEvent
import com.phat.testbase.dev.extensions.dispatchHidden
import com.phat.testbase.dev.extensions.isVisibleOnScreen
import com.phat.testbase.dev.extensions.lifecycle.ResultLifecycle
import com.phat.testbase.dev.extensions.lifecycle.ResultRegistry
import com.phat.testbase.dev.extensions.lifecycle.ViewLifecycleOwner
import com.phat.testbase.dev.utils.ScreenUtil
import com.phat.testbase.view.ui.main.MainActivity
import com.skydoves.bindables.BindingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.lang.ref.SoftReference

abstract class BaseMvvmFragment<T : ViewDataBinding, VM : BaseMvvmViewModel>(@LayoutRes private val contentLayoutId: Int) : BindingFragment<T>(contentLayoutId) {
    private val TAG: String = this.javaClass.simpleName

    companion object {
        private const val STATE_INVISIBLE = 1
        private const val STATE_VISIBLE = 0
        private const val STATE_NONE = -1
    }

    /**
     * use or not EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * check animation
     */
    var isInLeft: Boolean = false
    var isOutLeft: Boolean = false
    var isCurrentScreen: Boolean = false //@FragmentHelper

    lateinit var viewModel : VM

    val viewLife = ViewLifecycleOwner()

    val resultLife: ResultLifecycle = ResultRegistry()

    private val mLifeRegistry get() = viewLife.lifecycle
    private var mVisibleState = STATE_NONE

    private var isConnected = true

    private var mExitTime: Long = 0

    private var fragmentView: SoftReference<View>? = null

    private var isLowMemory = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        viewModel = activity?.let { ViewModelProvider(it) }?.get(getVM()) as VM
    }

    abstract fun getVM(): Class<VM>

    fun <T : ViewModel> getViewModel(className: Class<T>): T? {
        return activity?.let { ViewModelProvider(it)[className] }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val saveData = setSaveData()
        if (saveData != null) {
            outState.putAll(saveData)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (savedInstanceState != null) onLoadSaveData(savedInstanceState)
            startFlow()
            initData()
            initVM()
        } catch (e:Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    protected fun onLoadSaveData(savedInstanceState: Bundle) {}

    override fun onLowMemory() {
        super.onLowMemory()
        viewModel.clearAll()
        clearViews()
        onStart()
    }

    final override fun onStart() {
        super.onStart()
        if (isVisibleOnScreen()) {
            performStartFragment()
            mLifeRegistry.start()
            Timber.tag(TAG).i("Start")
        }
    }

    final override fun onResume() {
        super.onResume()
        if (isVisibleOnScreen()) {
            onFragmentResumed()
            mLifeRegistry.resume()
            Timber.tag(TAG).i("Resume")
        }
    }

    final override fun onPause() {
        super.onPause()
        if (isVisibleOnScreen()) {
            onFragmentPaused()
            mLifeRegistry.pause()
            Timber.tag(TAG).i("Pause")
            viewModel.clearAll()
        }
    }

    final override fun onStop() {
        super.onStop()
        if (isVisibleOnScreen()) {
            performStopFragment()
            mLifeRegistry.stop()
            Timber.tag(TAG).i("Stop")
            viewModel.clearAll()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        viewModel.clearAll()
    }

    override fun onDetach() {
        super.onDetach()
    }

    open fun startFlow(){}

    open fun initData(){}

    open fun initVM(){}

    protected open fun onFragmentStarted() {}

    protected open fun onFragmentStopped() {}

    protected open fun onFragmentResumed() {}

    protected open fun onFragmentPaused() {}

    private fun performStopFragment() {
        onFragmentStopped()
        mVisibleState = STATE_INVISIBLE
    }

    private fun performStartFragment() {
        onFragmentStarted()
        mVisibleState = STATE_VISIBLE
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (mVisibleState == (if (hidden) STATE_INVISIBLE else STATE_VISIBLE)) return

        if (hidden) {
            onFragmentPaused()
            mLifeRegistry.pause()
            performStopFragment()
            mLifeRegistry.stop()
            Timber.tag(TAG).i("Hide")
        } else {
            performStartFragment()
            mLifeRegistry.start()
            onFragmentResumed()
            mLifeRegistry.resume()
            Timber.tag(TAG).i("Show")
        }
        dispatchHidden(hidden)
    }

    /**
     * goto @baseFragment
     */
    fun pushFragment(baseFragment: BaseMvvmFragment<*, *>) {
        (activity as BaseMvvmActivity<*, *>).pushFragment(baseFragment)
    }

    /**
     * remove fragment in back stack
     */
    fun popFragment() {
        (activity as BaseMvvmActivity<*, *>).popFragment()
    }

    /**
     * goto @baseFragment fragment in back stack (it will remove back fragment in back stack)
     * ex: backstack[0,1,2,3] -> popToFragment(1) --> backstack[0,1] (fragment 2,3 will be removed)
     */
    fun popToFragment(baseFragment: BaseMvvmFragment<*, *>) {
        (activity as BaseMvvmActivity<*, *>).popToFragment(baseFragment)
    }

    /**
     * remove all fragment in back stack (except main fragments)
     */
    fun popToRootFragment() {
        (activity as BaseMvvmActivity<*, *>).popToRootFragment()
    }


    /**
     * No network status -> automatic reconnection operation with network status, subclass can override this method
     */
    open fun doReConnected() {

    }

    open fun doDisConnected() {

    }

    /**
     * Network Change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        isConnected = event.isConnected
        if (event.isConnected) {
            doReConnected()
        } else {
            doDisConnected()
        }
    }

    fun setSaveData(): Bundle? {
        return null
    }

    private fun clearViews() {
        if (fragmentView != null) fragmentView!!.clear()
        fragmentView = null
        isLowMemory = true
    }

    protected open fun myActivity(): MainActivity? {
        try {
            return activity as MainActivity?
        } catch (ignored: java.lang.Exception) {
        }
        return null
    }

    fun lockScreen(isLock: Boolean) {
        ScreenUtil.lockScreen(activity, isLock)
    }

}