package com.phat.testbase.di

import com.phat.testbase.dev.utils.ContainsUtils
import com.phat.testbase.network.TestService
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import okhttp3.Interceptor
import java.math.BigDecimal

object TestClient : BaseApiClient<TestService>() {

    override fun getBaseUrl() = ContainsUtils.baseUrlT

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
