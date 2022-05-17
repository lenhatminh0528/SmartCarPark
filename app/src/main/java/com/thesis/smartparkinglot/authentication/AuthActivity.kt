package com.thesis.smartparkinglot.authentication

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.thesis.smartparkinglot.R
import com.thesis.smartparkinglot.databinding.ActivityAuthBinding

class AuthActivity : AccountActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding

    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigation()
    }

    override fun initData() {

    }

    override fun setAction() {

    }

    private fun setupNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.registerInfoFragment, R.id.loginFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}