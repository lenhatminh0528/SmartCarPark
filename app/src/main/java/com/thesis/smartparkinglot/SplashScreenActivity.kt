package com.thesis.smartparkinglot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.thesis.smartparkinglot.authentication.AuthActivity
import com.thesis.smartparkinglot.dashboard.DashboardActivity
import com.thesis.smartparkinglot.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        Handler().postDelayed({
            alreadyLogin()
        },3000)
    }

    override fun setAction() {
    }

    private fun alreadyLogin() {
        val userId = AppShareRefs.getUserId(this)
        if(userId != null && userId !== ""){
            goToDashBoard(userId)
        }else {
            goToAuthScreen()
        }
    }

    private fun goToDashBoard(userId: String?) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun goToAuthScreen() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}