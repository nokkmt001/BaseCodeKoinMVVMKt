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

@file:Suppress("unused")

package com.phat.testbase

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.phat.testbase.database.realm.extension.RealmConfigStore
import com.phat.testbase.devphat.extensions.module.DependenceContext
import com.phat.testbase.di.*
import com.phat.testbase.network.GlobalResponseOperator
import com.skydoves.sandwich.SandwichInitializer
import com.phat.testbase.model.realm.UserModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DependenceContext.init(this)
        Hawk.init(this).build()
        Realm.init(this)
        val userAddressConfig = RealmConfiguration.Builder().name("user-db").schemaVersion(1)
            .deleteRealmIfMigrationNeeded().build()
        // clear previous data for fresh start
        Realm.deleteRealm(Realm.getDefaultConfiguration())
        Realm.deleteRealm(userAddressConfig)
        RealmConfigStore.initModule(UserModule::class.java, userAddressConfig)

        startKoin {
            androidContext(this@MainApplication)
            modules(networkModule)
            modules(viewModelModule)
        }

        // initialize global sandwich operator
        SandwichInitializer.sandwichOperator = GlobalResponseOperator<Any>(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
