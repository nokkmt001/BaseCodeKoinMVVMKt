package com.phat.testbase.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.phat.testbase.devphat.event.NetworkChangeEvent
import com.phat.testbase.devphat.utils.NetWorkUtil
import org.greenrobot.eventbus.EventBus

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        EventBus.getDefault().post(NetworkChangeEvent(isConnected))
    }

}