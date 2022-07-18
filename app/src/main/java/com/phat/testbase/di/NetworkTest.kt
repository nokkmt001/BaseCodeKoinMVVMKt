package com.phat.testbase.di

import com.phat.testbase.devphat.utils.ContainsUtils
import com.phat.testbase.network.RequestInterceptor
import com.phat.testbase.network.TestService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModuleTest = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor())
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(ContainsUtils.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(TestService::class.java) }
}