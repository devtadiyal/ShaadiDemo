package com.example.medcords.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.medcords.MainActivity
import com.example.medcords.R

class SplashActivity : AppCompatActivity() {

        private val SPLASH_TIME_OUT = 3000L
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash)
           /* val activity = if (it == null) AuthActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })*/
            Handler(Looper.getMainLooper()).postDelayed({
                    val i = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }, SPLASH_TIME_OUT)


    }
}