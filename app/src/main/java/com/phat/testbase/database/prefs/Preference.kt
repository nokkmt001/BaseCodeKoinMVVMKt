package com.phat.testbase.database.prefs

import com.orhanobut.hawk.Hawk
import com.phat.testbase.model.ProfileResponse

object Preference {

    private const val userLogin = "USER_LOGIN"

    /**
     * Save
     * **/
    fun saveUser(user: ProfileResponse?) {
        if (user == null) {
            clearUser()
        } else {
            Hawk.put(userLogin, user)
        }
    }

    /**
     * Get
     * **/
    fun getUser(): ProfileResponse? {
        return Hawk.get(userLogin)
    }

    /**
     * Clear
     * **/

    fun clearAll() {
        Hawk.deleteAll()
    }

    fun clearUser() {
        Hawk.delete(userLogin)
    }

}