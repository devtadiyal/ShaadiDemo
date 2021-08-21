package com.example.medcords

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.medcords.datastore.Preferences
import com.example.medcords.viewmodel.AuthViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity() , KodeinAware{

    //Kodein DI injecting factory class instance
    override val kodein by kodein()
    private val preferences: Preferences by instance()

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        val navController = navHostFragment.navController

        //getting saved value of HomeFragment in MainActivity using DataStore
        preferences.token.asLiveData().observe(this, androidx.lifecycle.Observer {
            //Toast.makeText(this, it ?: "value is null", Toast.LENGTH_SHORT).show()
        })
    }

    //back button handling
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}