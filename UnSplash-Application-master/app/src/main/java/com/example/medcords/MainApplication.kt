package com.example.medcords

import android.app.Application
import com.example.medcords.datastore.Preferences
import com.example.medcords.model.Result
import com.example.medcords.network.Api
import com.example.medcords.repository.Repository
import com.example.medcords.viewmodel.AuthViewModelFactory
import com.example.medcords.viewmodel.DetailsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : Application(),KodeinAware{
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))
        bind() from singleton { Api() }
        bind() from singleton { Repository(instance()) }
        bind() from singleton { Preferences(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { DetailsViewModelFactory(instance()) }

    }
}