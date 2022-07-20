package com.phat.testbase.view.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TestStateAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val countTab = 3

    override fun getCount(): Int  = countTab

    override fun getItem(position: Int): Fragment {
        return goToTab(position)!!
    }

    private fun goToTab(tabPosition: Int): Fragment? {
        var goFragment: Fragment? = null
        when (tabPosition) {
            0 -> goFragment = HomeFragment()
            1 -> goFragment = LibraryFragment()
            2 -> goFragment = RadioFragment()
        }
        return goFragment
    }
}