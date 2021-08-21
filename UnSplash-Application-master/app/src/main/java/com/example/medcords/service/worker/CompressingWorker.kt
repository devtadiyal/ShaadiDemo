package com.example.medcords.service.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CompressingWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {

            for (i in 0..5500) {
                Log.i("MYTAG", "COMPRESSING  $i")
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

}
