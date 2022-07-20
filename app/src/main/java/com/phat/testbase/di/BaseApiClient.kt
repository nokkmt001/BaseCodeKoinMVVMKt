package com.phat.testbase.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phat.testbase.dev.utils.ContainsUtils.TIME_OUT
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

abstract class BaseApiClient<Client> {

    private var apiClient: Client? = null   // provider interface api

    private fun getClient(url: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(logging)
        return getRetrofit(url, okHttpClient)
    }

    private fun getRetrofit(url: String, builder: OkHttpClient.Builder): Retrofit {

        builder.apply {
            connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(AuthenticationInterceptor())
        }

        return Retrofit.Builder().apply {
            baseUrl(url)
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            addConverterFactory(provideGsonConvertFactory())
            addConverterFactory(MoshiConverterFactory.create(moshi))
            client(builder.build())
        }.build()
    }

    fun getClient() : Client{
        if (apiClient == null) {
            apiClient = getClient(getBaseUrl()).create(getClassM())
        }
        return apiClient!!
    }

    val moshi = Moshi.Builder()
        .add(BigDecimalAdapter)
        .build()

    object BigDecimalAdapter {
        @FromJson
        fun fromJson(string: String) = BigDecimal(string)

        @ToJson
        fun toJson(value: BigDecimal) = value.toString()
    }

    abstract fun getClassM(): Class<Client>

    private fun provideGsonConvertFactory() = GsonConverterFactory
        .create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())

    abstract fun getBaseUrl() : String

    abstract fun AuthenticationInterceptor(): Interceptor

}