package com.phat.testbase.model.realm

import io.realm.RealmObject

/**
 * Created by victor on 2/1/17.
 */
open class Item() : RealmObject() {

    var name: String = ""

    constructor(name: String) : this() {
        this.name = name
    }
}