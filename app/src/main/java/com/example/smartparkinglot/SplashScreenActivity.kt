package com.example.smartparkinglot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.dashboard.DashboardActivity
import com.example.smartparkinglot.databinding.ActivitySplashScreenBinding

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
        TODO("Not yet implemented")
    }

    private fun alreadyLogin() {
        val userId = AppShareRefs.getUserId(this)
        if(userId != null){
            goToDashBoard()
        }else {
            goToAuthScreen()
        }
    }

    private fun goToDashBoard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun goToAuthScreen() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}