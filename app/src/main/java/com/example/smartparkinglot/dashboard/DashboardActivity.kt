package com.example.smartparkinglot.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartparkinglot.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(R.layout.activity_dashboard) {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupNavigation()
    }

    private fun setupNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.dashboard_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.userInfoFragment, R.id.qrCodeFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_nav?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }
}