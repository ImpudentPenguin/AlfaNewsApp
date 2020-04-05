package com.elenamakeeva.alfanewsapp.logic.api

import com.elenamakeeva.alfanewsapp.api.News
import io.reactivex.Observable
import retrofit2.http.GET

interface NewsApi {

    @GET("_/rss/_rss.html?subtype=1&category=2&city=21")
    fun getNews(): Observable<News>
}