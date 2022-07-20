package com.phat.testbase.network

import com.phat.testbase.model.TestBanner
import retrofit2.Call
import retrofit2.http.GET

interface TestService {

    @GET("api/banner")
    fun getBannerAsync(): Call<TestBanner>
}