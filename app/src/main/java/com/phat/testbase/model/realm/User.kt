package com.phat.testbase.model.realm

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmModule

@RealmClass
open class User(var name: String? = null, var address: Address? = Address()) : RealmModel

open class Address(var street: String? = null, var city: String? = null, var zip: String? = null) : RealmObject()

@RealmModule(classes = [(User::class), (Address::class)])
class UserModule