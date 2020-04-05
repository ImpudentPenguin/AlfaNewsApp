package com.elenamakeeva.alfanewsapp.model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.impl.Scheduler
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item
import com.elenamakeeva.alfanewsapp.api.News
import com.elenamakeeva.alfanewsapp.logic.api.ApiServices
import com.elenamakeeva.alfanewsapp.logic.database.ItemsDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.stream.Collectors

@SuppressLint("CheckResult")
class NewsViewModel (application: Application) : AndroidViewModel(application) {
    private val apiServices = ApiServices()
    private var viewStateNewsList = MutableLiveData<List<Item>>()
    private var viewStateFavNews = MutableLiveData<FavouriteNews>()
    private var viewStateLinkList = MutableLiveData<List<String>>()
    private var viewStateFavouriteNewsList = MutableLiveData<List<FavouriteNews>>()
    var database: ItemsDatabase

    val viewState: LiveData<List<Item>> = viewStateNewsList
    init {
        database = ItemsDatabase.getInstance(application)
    }


    private fun onLoadNews() {
        apiServices.getNews()
            .map(News::channel)
          //  .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                insertDatabase(it)
                updateLinkList(it.itemList!!)
            }, {
                processError(it)
            })
    }

    fun getAllNews() : LiveData<List<Item>> {
        onLoadNews()
        database.itemsDao().getAllNews()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
            viewStateNewsList.postValue(it)
        }, {
            processError(it)
        })
        return viewStateNewsList
    }

    fun getAllFavouriteNews() : LiveData<List<FavouriteNews>> {
        database.itemsDao().getAllFavouriteNews()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
            viewStateFavouriteNewsList.postValue(it)
                Log.i("infoAlfa", it.isNullOrEmpty().toString())
        }, {
            processError(it)
        })
        return viewStateFavouriteNewsList
    }

    fun getFavouriteNews(id: Int) : LiveData<FavouriteNews> {
        database.itemsDao().getFavouriteNews(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewStateFavNews.postValue(it)
            },{
                processError(it)
            })
        return viewStateFavNews
    }


    fun insertFavouriteNews(favNews: FavouriteNews) {
        Completable.fromAction {
            database.itemsDao().insertFavouriteItem(favNews) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun insertDatabase(channel: News.Channel?) {
        if(channel == null) processError(NullPointerException())
        for(id in 0 until channel!!.itemList!!.size) {
            channel.itemList?.get(id)?.let { database.itemsDao().insertItem(it) }
        }
    }

    private fun updateLinkList(itemList: List<Item>) = viewStateLinkList.postValue(itemList.stream().map(Item::link).collect(Collectors.toList()))

    private fun processError(throwable: Throwable) {
        Log.e("ErrorAlfaNewsApp", throwable.message!!)
    }

}