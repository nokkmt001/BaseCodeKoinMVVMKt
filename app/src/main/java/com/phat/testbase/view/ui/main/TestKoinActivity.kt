package com.phat.testbase.view.ui.main

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.phat.testbase.R
import com.phat.testbase.database.realm.extension.*
import com.phat.testbase.databinding.ActivityTestKoinBinding
import com.phat.testbase.devphat.extensions.applyExitMaterialTransform
import com.phat.testbase.devphat.extensions.isMainThread
import com.phat.testbase.devphat.extensions.wait
import com.phat.testbase.model.realm.Address
import com.phat.testbase.model.realm.Item
import com.phat.testbase.model.realm.User
import com.skydoves.bindables.BindingActivity
import io.realm.Realm

class TestKoinActivity: BindingActivity<ActivityTestKoinBinding>(R.layout.activity_test_koin) {

    private val dbSize = 100
    private val userSize = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        applyExitMaterialTransform()
        super.onCreate(savedInstanceState)
        performTest("main thread") {
            Thread {
                performTest("background thread items") {
                    // User perform Test
                    performUserTest("main thread users") {
                        Thread { performUserTest("background thread users") }.start()
                    }
                }
            }.start()
        }

    }

    private fun performUserTest(threadName: String, finishCallback: (() -> Unit)? = null) {

        addMessage("Starting test on $threadName with User realm configuration", important = true)

        deleteAll<User>()
        populateUserDb(userSize)

        addMessage("DB populated with $userSize users")

        addMessage("Querying users on $threadName...")

        addMessage("Result: ${User().queryAll().size} items ")
        addMessage("Result: ${queryAll<User>().size} items ")

        addMessage("Deleting users on $threadName...")

        deleteAll<User>()

        addMessage("Querying users on $threadName...")

        addMessage("Result: ${User().queryAll().size} items ")

        addMessage("Observing table changes...")

        val subscription = User().queryAllAsFlowable().subscribe {
            addMessage("Changes received on ${if (Looper.myLooper() == Looper.getMainLooper()) "main thread" else "background thread"}, total items: " + it.size)
        }

        wait(1) {
            populateUserDb(10)
        }

        wait(if (isMainThread()) 2 else 1) {
            populateUserDb(10)
        }

        wait(if (isMainThread()) 3 else 1) {
            populateUserDb(10)
        }

        wait(if (isMainThread()) 4 else 1) {
            subscription.dispose()
            addMessage("Subscription finished")
            var defaultCount = Realm.getDefaultInstance().where(User::class.java).count()
            var userCount = User().getRealmInstance().where(User::class.java).count()

            addMessage("All users from default configuration : $defaultCount")
            addMessage("All users from user configuration : $userCount")
            finishCallback?.invoke()
        }
    }

    private fun performTest(threadName: String, finishCallback: (() -> Unit)? = null) {

        addMessage("Starting test on $threadName...", important = true)

        deleteAll<Item>()
        populateDB(numItems = dbSize)

        addMessage("DB populated with $dbSize items")

        addMessage("Querying items on $threadName...")

        addMessage("Result: ${Item().queryAll().size} items ")
        addMessage("Result: ${queryAll<Item>().size} items ")

        addMessage("Deleting items on $threadName...")

        Item().deleteAll()

        addMessage("Querying items on $threadName...")

        addMessage("Result: ${Item().queryAll().size} items ")

        addMessage("Observing table changes...")

        val subscription = Item().queryAllAsFlowable().subscribe {
            addMessage("Changes received on ${if (Looper.myLooper() == Looper.getMainLooper()) "main thread" else "background thread"}, total items: " + it.size)
        }
        wait(1) {
            populateDB(numItems = 10)
        }

        wait(if (isMainThread()) 2 else 1) {
            populateDB(numItems = 10)
        }

        wait(if (isMainThread()) 3 else 1) {
            populateDB(numItems = 10)
        }

        wait(if (isMainThread()) 4 else 1) {
            subscription.dispose()
            addMessage("Subscription finished")
            finishCallback?.invoke()
        }
    }

    private fun populateUserDb(numUsers: Int) {
        Array(numUsers) { User("name_%d".format(it), Address("street_%d".format(it))) }.saveAll()
    }

    private fun populateDB(numItems: Int) {
        Array(numItems) { Item() }.saveAll()
    }

    private fun addMessage(message: String, important: Boolean = false) {
        Handler(Looper.getMainLooper()).post {
            val view = TextView(this)
            if (important) view.typeface = Typeface.DEFAULT_BOLD
            view.text = message
            binding.mainContainer.addView(view)
            binding.scroll.smoothScrollBy(0, 1000)
        }
    }
}