package com.example.medcords.ui.serviceui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.medcords.R
import com.example.medcords.service.BoundService
import kotlinx.android.synthetic.main.fragment_client.*


class ClientFragment : Fragment() {

    var boundService: BoundService? = null
    var isBound = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().bindService(
            Intent(activity, BoundService::class.java),
            myConnection,
            Context.BIND_AUTO_CREATE
        )

        showtime.setOnClickListener {
            showTime(it)
        }

        openworkmanager.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_clientFragment_to_uploadFragment)
        }
    }

    fun showTime(view: View) {
        val currentTime = boundService?.getCurrentTime()
        time.text = currentTime
    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as BoundService.MyLocalBinder
            boundService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
}