package com.phat.testbase.view.ui.test

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.phat.testbase.R
import com.phat.testbase.view.ui.main.MainFragment
import com.skydoves.bindables.BindingFragment

class TestMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
        loadFragment(MainFragment())
    }

    private fun loadFragment(fragment: BindingFragment<*>) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerFragment, fragment)
            commit()
        }
    }
}