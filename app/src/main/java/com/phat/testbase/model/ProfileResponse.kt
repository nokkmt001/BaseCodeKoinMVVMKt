package com.phat.testbase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponse(
    var note: String? = null,
) : Parcelable
