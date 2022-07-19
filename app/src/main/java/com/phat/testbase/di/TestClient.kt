package com.phat.testbase.di

import com.phat.testbase.devphat.utils.ContainsUtils
import com.phat.testbase.network.TestService
import okhttp3.Interceptor

object TestClient : BaseApiClient<TestService>() {

    override fun getBaseUrl() = ContainsUtils.baseUrl

    override fun AuthenticationInterceptor(): Interceptor {
        return authInterceptor
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(ContainsUtils.HEADER_UUID, java.util.UUID.randomUUID().toString())
            .addHeader(ContainsUtils.HEADER_LANG, ContainsUtils.DEFAULT_LANGUAGE)
            .addHeader(ContainsUtils.HEADER_CONTENT_TYPE, ContainsUtils.HEADER_CONTENT_TYPE_VALUE_JSON)
            .addHeader(ContainsUtils.HEADER_AUTHORIZATION, ContainsUtils.API_KEY_TOKEN)
            .build()
        chain.proceed(newRequest)
    }

    override fun getClassM(): Class<TestService> = TestService::class.java

}
