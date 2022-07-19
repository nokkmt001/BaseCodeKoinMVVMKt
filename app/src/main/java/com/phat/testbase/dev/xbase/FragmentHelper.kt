package com.phat.testbase.dev.xbase

import com.phat.testbase.R
import java.util.*

class FragmentHelper(private var fragmentAction: FragmentAction?) {
    var pageList: ArrayList<Stack<BaseMvvmFragment<*, *>>>? = null
    var pageIndex: Int = 0
    var pageSize: Int = 0
    var layoutId: Int = 0
    private var childPage = -1
    private var fragmentManager: androidx.fragment.app.FragmentManager? = null

    var buildFragment: Array<BaseMvvmFragment<*, *>?>

    val currentPageSize: Int
        get() = pageList!![pageIndex].size

    fun getBuildFragments(): Array<BaseMvvmFragment<*, *>?> {
        return buildFragment
    }

    init {
        this.layoutId = fragmentAction!!.containerLayoutId
        this.fragmentManager = fragmentAction!!.setFragmentManager()
        this.buildFragment = fragmentAction!!.initFragment()
        initFragments(buildFragment)
    }

    private fun initFragments(fragments: Array<BaseMvvmFragment<*, *>?>) {
        this.pageList = ArrayList(fragments.size)
        pageSize = fragments.size

        for (fragment in fragments) {
            val stack = Stack<BaseMvvmFragment<*, *>>()
            stack.push(fragment)
            this.pageList!!.add(stack)
        }

        val fragment = pageList!![pageIndex].peek()
        if (fragment.isAdded || fragment.isDetached) {
            this.showFragment(pageIndex)
        } else {
            val transaction = fragmentManager!!.beginTransaction()
            transaction.add(layoutId, fragment)
            transaction.commit()
        }
    }

    fun peek(): BaseMvvmFragment<*, *> {
        return pageList!![pageIndex].peek()
    }

    fun pushFragment(fragment: BaseMvvmFragment<*, *>) {
        val hideFragment = pageList!![pageIndex].peek()
        pageList!![pageIndex].push(fragment)

        fragment.isCurrentScreen = true
        hideFragment.isCurrentScreen = false

        val transaction = fragmentManager!!.beginTransaction()
        transaction.add(layoutId, fragment)
        transaction.hide(hideFragment)
        transaction.commit()

    }

    fun replaceFragment(self: BaseMvvmFragment<*, *>, fragment: androidx.fragment.app.Fragment) {
        val fragmentManager = self.fragmentManager
        fragmentManager!!.beginTransaction()
            .setCustomAnimations(
                R.anim.push_up_in,
                R.anim.push_up_out,
                0, 0
            )
            .replace(layoutId, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun popFragmentToRoot(): Boolean {
        val level = pageList!![pageIndex].size - 1
        return popFragment(level)
    }

    @JvmOverloads
    fun popFragment(position: Int = 1): Boolean {
        var level = position
        if (level <= 0) return false
        if (pageList!![pageIndex].size <= level) return false
        val transaction = fragmentManager!!.beginTransaction()

        while (level >= 1) {
            val fragment = pageList!![pageIndex].pop()
            fragment.isCurrentScreen = false
            transaction.remove(fragment)
            level--
        }
        val showFragment = pageList!![pageIndex].peek()
        showFragment.isCurrentScreen = true
        transaction.show(showFragment)
        transaction.commit()
        return true
    }

    fun <E : BaseMvvmFragment<*, *>> popToFragment(aClass: Class<E>): Boolean {
        val level = findIndexFragmentChild(aClass)
        return popFragment(level)
    }

    fun <E : BaseMvvmFragment<*, *>> findIndexFragmentChild(aClass: Class<E>): Int {
        val size = currentPageSize
        var level = 0
        for (i in size - 1 downTo 0) {
            val BaseFragment = pageList!![pageIndex][i]
            if (aClass.simpleName == BaseFragment.javaClass.simpleName) {
                return level
            } else {
                level++
            }
        }
        return -1
    }

    fun showFragment(index: Int) {
        if (index == pageIndex) return
        val showFragment = pageList!![index].peek()
        val hideFragment = pageList!![pageIndex].peek()
        val transaction = fragmentManager!!.beginTransaction()

        if (pageIndex > index) {
            showFragment.isInLeft = true
            hideFragment.isOutLeft = false
        } else {
            showFragment.isInLeft = false
            hideFragment.isOutLeft = true
        }
        showFragment.isCurrentScreen = true
        hideFragment.isCurrentScreen = false

        // TODO: Khong refresh lai UI, ko refresh lai data
        if (showFragment.isDetached || showFragment.isAdded) {
            transaction.show(showFragment)
        } else {
            transaction.add(layoutId, showFragment)
        }
        transaction.hide(hideFragment)

        transaction.commit()
        pageIndex = index
    }

    fun getPageSizeByIndex(index: Int): Int {
        return pageList!![index].size
    }

    fun setChildPage(childPage: Int) {
        this.childPage = childPage
    }

    fun remove() {
        buildFragment = arrayOf(null)
        fragmentAction = null
        fragmentManager = null
    }

    interface FragmentAction {
        fun initFragment(): Array<BaseMvvmFragment<*, *>?>
        var containerLayoutId: Int
        fun setFragmentManager(): androidx.fragment.app.FragmentManager
    }
}
