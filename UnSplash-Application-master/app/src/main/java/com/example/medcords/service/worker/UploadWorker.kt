package com.example.medcords.service.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.medcords.ui.workmanagerui.UploadFragment
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {
            //get send data value in worker class using key
            val count = inputData.getInt(UploadFragment.KEY_COUNT_VALUE, 0)
            //pass getting data
            for (i in 0..count) {
                Log.i("MYTAG", "UPLOADING $i")
            }

            //now pass data from worker class to fragment same process using data class
            val timeFormat =  SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US)
            val outputData = Data.Builder().putString(WORKER_KEY,timeFormat.format(Date()).toString()).build()
            //pass outData to Result.success(outputData)
            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }

    }

    companion object{
        const val WORKER_KEY = "worker_key"
    }

}
