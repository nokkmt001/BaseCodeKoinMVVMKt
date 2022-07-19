package com.phat.testbase.devphat.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phat.testbase.devphat.extensions.module.DependenceContext

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependenceContext.sInstance.lookup(modelClass)!!.instance as T
    }

    companion object {
        var sInstance = ViewModelFactory()
    }
}
