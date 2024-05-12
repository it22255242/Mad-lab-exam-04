package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codility.todoapp.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 2000 // Splash screen duration in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        // Delayed transition to the main activity
        Handler().postDelayed({
            // Start main activity and finish splash activity
            val mainIntent = Intent(this@SplashActivity, ScrollActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

}