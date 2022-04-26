package com.example.smartparkinglot.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.smartparkinglot.BaseActivity
import com.example.smartparkinglot.R
import com.example.smartparkinglot.custom.ConfirmationDialog
import com.example.smartparkinglot.databinding.ActivityAuthBinding
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_dashboard.*

class AuthActivity : BaseActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding
    override fun bindingView() {
        supportActionBar?.hide()
        AndroidInjection.inject(this)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigation()
    }
    override fun initData() {}
    override fun setAction() {}
    private fun setupNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.registerInfoFragment, R.id.loginFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}