package com.example.paulina.assignmentapplication.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Paulina on 2018-03-08.
 */

@Module
class ConnectionModule {

    companion object {
        const val URL = "http://www.godt.no/api/"
    }

    @Provides
    @Singleton
    fun provideUrl(): String = URL


    @Provides
    @Singleton
    fun provideRetrofitClient(baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    @Singleton
    fun provideNetworkService(retrofit: Retrofit): NetworkService =
            retrofit.create(NetworkService::class.java)



    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()


}