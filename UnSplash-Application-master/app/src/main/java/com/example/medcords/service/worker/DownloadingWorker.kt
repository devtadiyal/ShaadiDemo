package com.example.medcords.service.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class DownloadingWorker (context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {

            for (i in 0..5500) {
                Log.i("MYTAG", "DOWNLOADING  $i")
            }
            //pass data to fragment class from worker
            val timeFormat =  SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US)
            val outputData = Data.Builder().putString(DownloadingWorker.DOWNLOAD_KEY,timeFormat.format(Date()).toString()).build()


            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }

    }
    companion object{
        const val DOWNLOAD_KEY = "download_key"
    }
}