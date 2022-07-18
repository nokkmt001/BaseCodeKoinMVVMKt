package com.phat.testbase.network

import com.phat.testbase.model.ProfileResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface TestService {

    @GET("profile")
    suspend fun getProfileAsync(@HeaderMap header: HashMap<String, Any>): Deferred<Call<ProfileResponse>>
}