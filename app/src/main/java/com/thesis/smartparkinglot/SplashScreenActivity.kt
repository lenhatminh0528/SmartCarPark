package com.thesis.smartparkinglot

import android.content.Intent
import android.os.Handler
import com.thesis.smartparkinglot.authentication.AccountActivity
import com.thesis.smartparkinglot.authentication.AuthActivity
import com.thesis.smartparkinglot.dashboard.DashboardActivity
import com.thesis.smartparkinglot.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AccountActivity() {
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
        finish()
    }

    private fun goToDashBoard(userId: String?) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun goToAuthScreen() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}