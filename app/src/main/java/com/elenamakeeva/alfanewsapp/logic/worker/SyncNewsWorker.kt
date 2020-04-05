package com.elenamakeeva.alfanewsapp.logic.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class SyncNewsWorker(val context: Context,  workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        return try {
            TimeUnit.MINUTES.sleep(10)
            Result.success()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Result.failure()
        }
    }
}