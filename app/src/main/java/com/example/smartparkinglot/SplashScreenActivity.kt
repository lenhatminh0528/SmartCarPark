package com.example.smartparkinglot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.dashboard.DashboardActivity

class SplashScreenActivity : AppCompatActivity(R.layout.activity_splash_screen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        },3000)
    }
}