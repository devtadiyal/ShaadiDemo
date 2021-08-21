package com.example.medcords.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preferences(context: Context){

    //creating application level context to use datastore object in application
private val applicationContext = context.applicationContext

    //creating instance of datastore object
    private val dataStore : DataStore<Preferences>

    init {
        //initialize datastore object
        dataStore = applicationContext.createDataStore(name = "datastore")
    }

    //funtion to save data
    suspend fun saveData(str:String){
        dataStore.edit {preference->
            //by assigning saving str value inside key
            preference[KEY] = str
        }
    }

    //for getData
    val token : Flow<String?>
    get() = dataStore.data.map {preferences->
        preferences[KEY]
    }

    //inside the companion object defining keyname of value
    companion object{
        private val KEY = preferencesKey<String>("token")
    }
}