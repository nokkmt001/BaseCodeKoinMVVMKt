package com.phat.testbase.dev.xbase.adapter5

import android.view.View
import android.view.ViewGroup

abstract class OnViewHolderInflated {
    abstract fun onInflated(view: View, parent: ViewGroup, viewType: Int)
}