package com.elenamakeeva.alfanewsapp.logic.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elenamakeeva.alfanewsapp.api.News
import com.elenamakeeva.alfanewsapp.logic.api.ApiServices
import com.elenamakeeva.alfanewsapp.logic.database.ItemsDatabase

class SyncNewsWorker(val context: Context,  workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiServices = ApiServices()
    private var database: ItemsDatabase = ItemsDatabase.getInstance(context)

    @SuppressLint("CheckResult")
    override fun doWork(): Result {
        return try {
            apiServices.getNews()
                .map(News::channel)
                .subscribe({
                    for(id in 0 until it.itemList!!.size) {
                        it.itemList?.get(id)?.let { database.itemsDao().insertItem(it) }
                    }
                }, {
                    Log.e("ErrorAlfaNewsApp", it.message!!)
                })
            Result.success()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Result.failure()
        }
    }
}