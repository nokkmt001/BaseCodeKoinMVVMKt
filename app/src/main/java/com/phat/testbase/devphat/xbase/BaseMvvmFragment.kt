package com.phat.testbase.devphat.xbase

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.phat.testbase.devphat.event.NetworkChangeEvent
import com.phat.testbase.devphat.extensions.ViewModelFactory
import com.phat.testbase.devphat.extensions.dispatchHidden
import com.phat.testbase.devphat.extensions.getFirstGenericParameter
import com.phat.testbase.devphat.extensions.isVisibleOnScreen
import com.phat.testbase.devphat.extensions.lifecycle.ResultLifecycle
import com.phat.testbase.devphat.extensions.lifecycle.ResultRegistry
import com.phat.testbase.devphat.extensions.lifecycle.ViewLifecycleOwner
import com.skydoves.bindables.BindingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseMvvmFragment<T : ViewDataBinding, VM : BaseMvvmViewModel>(@LayoutRes private val contentLayoutId: Int) : BindingFragment<T>(contentLayoutId)  {
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

    var isConnected = true

    private var mExitTime: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }

        viewModel = ViewModelProvider(this, ViewModelFactory.sInstance).get(getFirstGenericParameter()) as VM
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startFlow()
        initData()
        initVM()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    final override fun onStart() {
        super.onStart()
        if (isVisibleOnScreen()) {
            performStartFragment()
            mLifeRegistry.start()
            Log.i(TAG, "Start")
        }
    }

    final override fun onResume() {
        super.onResume()
        if (isVisibleOnScreen()) {
            onFragmentResumed()
            mLifeRegistry.resume()
            Log.i(TAG, "Resume")
        }
    }

    final override fun onPause() {
        super.onPause()
        if (isVisibleOnScreen()) {
            onFragmentPaused()
            mLifeRegistry.pause()
            Log.i(TAG, "Pause")
        }
    }

    final override fun onStop() {
        super.onStop()
        if (isVisibleOnScreen()) {
            performStopFragment()
            mLifeRegistry.stop()
            Log.i(TAG, "Stop")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun startFlow(){}

    fun initData(){}

    fun initVM(){}

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
            Log.i(TAG, "Hide")
        } else {
            performStartFragment()
            mLifeRegistry.start()
            onFragmentResumed()
            mLifeRegistry.resume()
            Log.i(TAG, "Show")
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

}