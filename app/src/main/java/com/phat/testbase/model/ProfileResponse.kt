package com.phat.testbase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponse(
    var mailers: List<String>? = null,

    var almost_expired_date: String? = null,

    var almost_expired_point: Int = 0,

    var object_version: Int = 0,

    var career_code: String? = null,

    var note: String? = null,

    var city: String? = null,

    var district: String? = null,

    var ward: String? = null,

    var street: String? = null,

    var nationality: String? = null,

    var address: String? = null,

    var email: String? = null,

    var date_of_birth: String? = null,

    var gender: Int = 0,

    var full_name: String? = null,

    var avatar: String? = null,

    var point: Int? = null,

    var password: String? = null,

    var phone_number: String? = null,

    var passport: String? = null,

    var voucher_amount: Int = 0,

    var id: Int = 0,

    var code: String? = null,

    var storeCode: Int? = null,

    var store: Int? = null,

    var store_shopping: Int? = null,

    private val area_code: String? = null,

    private val mbrIcType: String? = null,
) : Parcelable
