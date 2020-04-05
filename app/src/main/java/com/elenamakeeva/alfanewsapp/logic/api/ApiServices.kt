package com.elenamakeeva.alfanewsapp.logic.api

import com.elenamakeeva.alfanewsapp.api.News
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApiServices {
    companion object {
        const val URL = "https://alfabank.ru/"
        val TIMEOUT = TimeUnit.SECONDS.toMillis(60)
    }

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .baseUrl(URL)
        .client(createClientBuilder())
        .build()

    private fun createClientBuilder() = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .addInterceptor(createLogging())
        .build()

    private fun createLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private fun createNewsApi() : NewsApi = retrofit.create(NewsApi::class.java)

    fun getNews() = createNewsApi().getNews()
}