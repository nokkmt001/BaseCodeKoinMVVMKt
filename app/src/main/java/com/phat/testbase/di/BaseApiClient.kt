package com.phat.testbase.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phat.testbase.devphat.utils.ContainsUtils.TIME_OUT
import com.phat.testbase.devphat.extensions.getFirstGenericParameter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            client(builder.build())
        }.build()
    }

    protected fun getClient() : Client{
        if (apiClient == null) {
            apiClient = getClient(getBaseUrl()).create(getClassM())
        }
        return apiClient!!
    }

    abstract fun getClassM(): Class<Client>

    private fun provideGsonConvertFactory() = GsonConverterFactory
        .create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())

    abstract fun getBaseUrl() : String

    abstract fun AuthenticationInterceptor(): Interceptor

}