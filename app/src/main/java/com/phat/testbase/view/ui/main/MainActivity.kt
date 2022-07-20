/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phat.testbase.view.ui.main

import android.os.Looper
import com.phat.testbase.R
import com.phat.testbase.databinding.ActivityMainBinding
import com.phat.testbase.dev.extensions.gone
import com.phat.testbase.dev.extensions.visible
import com.phat.testbase.dev.xbase.BaseMvvmActivity
import com.phat.testbase.dev.xbase.EmptyViewModel
import com.skydoves.bindables.BindingFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Handler

class MainActivity : BaseMvvmActivity<ActivityMainBinding, EmptyViewModel>(R.layout.activity_main) {
    private val splashFragment = SplashFragment()

    override fun startFlow() {
        super.startFlow()
        showMain(false)
        loadFragment(splashFragment)

        binding {
            pagerAdapter = MainPagerAdapter(this@MainActivity)
//            vm = getViewModel()
        }

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            showMain(true)
        }, 2000)

    }

    override fun getVM(): Class<EmptyViewModel> = EmptyViewModel::class.java

    private fun showMain(isShow:Boolean) {
        if (isShow) {
            containerMain.visible()
            containerLayout.gone()
        } else {
            containerMain.gone()
            containerLayout.visible()
        }

    }

    private fun loadFragment(fragment: BindingFragment<*>) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerLayout, fragment)
            commit()
        }
    }
}
