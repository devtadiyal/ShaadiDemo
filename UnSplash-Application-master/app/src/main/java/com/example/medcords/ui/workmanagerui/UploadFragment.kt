package com.example.medcords.ui.workmanagerui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.medcords.R
import com.example.medcords.service.worker.CompressingWorker
import com.example.medcords.service.worker.DownloadingWorker
import com.example.medcords.service.worker.FilterWorker
import com.example.medcords.service.worker.UploadWorker
import kotlinx.android.synthetic.main.fragment_upload.*
import java.util.concurrent.TimeUnit

class UploadFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        oneTime.setOnClickListener {
            oneTimeWorkRequest()
        }

        periodicTask.setOnClickListener {
            periodicWorkRequest()
        }
    }

    private fun oneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(requireContext())
        //data class for send data to worker and pass this data class in one time request
        val data: Data = Data.Builder().putInt(KEY_COUNT_VALUE, 100).build()

        //run workmanager on the basic of constraint eg charging enable than work start (if button click but charging not enable than work goes to enqued state)
        val constraint = Constraints.Builder().setRequiresCharging(true)
            //constraint for nw connected work will be execute
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        //creating new worker class request
        val filterRequest = OneTimeWorkRequest.Builder(FilterWorker::class.java).build()
        val compressRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java).build()

        //pass constraint on one time request also// passing data object to worker class
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraint)
            .setInputData(data).build()
        //running only single worker class
        // workManager.enqueue(uploadRequest)

        //running multiple worker class synchronized way using chaining
        // workManager.beginWith(filterRequest).then(compressRequest).then(uploadRequest).enqueue()

        //running multiple worker class asynchronous way using chaining
        //create mutable list and add all worker to it and pass to workmanager
        val parallalwork =
            mutableListOf<OneTimeWorkRequest>(filterRequest, compressRequest, uploadRequest)
        workManager.enqueue(parallalwork)


        //to get the status of work
        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(viewLifecycleOwner, Observer {
            workstatus.text = it.toString()

            //getting result from worker class to fragment
            if (it.state.isFinished) {
                val data: Data = it.outputData
                val msg = data.getString(UploadWorker.WORKER_KEY)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun periodicWorkRequest() {
        val workManager = WorkManager.getInstance(requireContext())

        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java,10,TimeUnit.SECONDS)
                .build()
        workManager.enqueue(periodicWorkRequest)
        //to get the status of work
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(viewLifecycleOwner, Observer {
            workstatus.text = it.toString()

            //getting result from worker class to fragment
            if (it.state.isFinished) {
                val data: Data = it.outputData
                val msg = data.getString(DownloadingWorker.DOWNLOAD_KEY)
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }

        })
    }


    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }
}