package com.example.medcords.service.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.medcords.ui.workmanagerui.UploadFragment
import java.text.SimpleDateFormat
import java.util.*

class FilterWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {

            for (i in 0..5500) {
                Log.i("MYTAG", "FILTERING  $i")
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

}
