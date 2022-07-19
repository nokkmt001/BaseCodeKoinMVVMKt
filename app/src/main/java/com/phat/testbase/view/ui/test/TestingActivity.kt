package com.phat.testbase.view.ui.test

import com.phat.testbase.R
import com.phat.testbase.databinding.ActivityContainerBinding
import com.phat.testbase.dev.xbase.BaseMvvmActivity
import com.phat.testbase.dev.xbase.EmptyViewModel
import com.phat.testbase.view.ui.main.MainFragment
import com.skydoves.bindables.BindingFragment

class TestingActivity : BaseMvvmActivity<ActivityContainerBinding, EmptyViewModel>(R.layout.activity_container) {

    override fun getVM(): Class<EmptyViewModel> = EmptyViewModel::class.java

    override fun startFlow() {
        super.startFlow()
        loadFragment(MainFragment())
    }

    private fun loadFragment(fragment: BindingFragment<*>) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerFragment, fragment)
            commit()
        }
    }

}
