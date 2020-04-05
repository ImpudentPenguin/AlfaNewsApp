package com.elenamakeeva.alfanewsapp.model

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item
import com.elenamakeeva.alfanewsapp.api.News
import com.elenamakeeva.alfanewsapp.logic.api.ApiServices
import com.elenamakeeva.alfanewsapp.logic.database.ItemsDatabase
import com.elenamakeeva.alfanewsapp.logic.worker.SyncNewsWorker
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class NewsViewModel (application: Application) : AndroidViewModel(application) {
    private val apiServices = ApiServices()
    private var database: ItemsDatabase = ItemsDatabase.getInstance(application)

    val viewState: LiveData<List<Item>> = database.itemsDao().getAllNews()
    val viewStateFavNewsList: LiveData<List<FavouriteNews>> = database.itemsDao().getAllFavouriteNews()

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeSyncDataWork = OneTimeWorkRequest.Builder(SyncNewsWorker::class.java).build()
        WorkManager.getInstance(application).enqueue(oneTimeSyncDataWork)

        val periodicSyncDataWork = PeriodicWorkRequest.Builder(SyncNewsWorker::class.java, 15, TimeUnit.MINUTES)
            .addTag("sync news")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "sync work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncDataWork
        )
    }

    fun onLoadNews() : Disposable {
        return apiServices.getNews()
            .map(News::channel)
            .subscribe({
                insertAllNews(it)
            }, {
                processError(it)
            })
    }

    fun getFavouriteNews(id: Int) : LiveData<FavouriteNews> = database.itemsDao().getFavouriteNews(id)

    fun insertFavouriteNews(favNews: FavouriteNews) {
        Completable.fromAction {
            database.itemsDao().insertFavouriteItem(favNews)}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun deleteFavouriteNews(favNews: FavouriteNews) {
        Completable.fromAction {
            database.itemsDao().deleteFavouriteItem(favNews)}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun insertAllNews(channel: News.Channel?) {
        if(channel == null) processError(NullPointerException())
        for(id in 0 until channel!!.itemList!!.size) {
            channel.itemList?.get(id)?.let { database.itemsDao().insertItem(it) }
        }
    }

    private fun processError(throwable: Throwable) = Log.e("ErrorAlfaNewsApp", throwable.message!!)
}