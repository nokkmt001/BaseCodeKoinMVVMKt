package com.phat.testbase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestBanner(
    var data : MutableList<ResponBanner>? = null
) : Parcelable

@Parcelize
data class ResponBanner(
    var title: String? = "",
    var slug: String? = "",
    var photo: String? = "",
) : Parcelable
