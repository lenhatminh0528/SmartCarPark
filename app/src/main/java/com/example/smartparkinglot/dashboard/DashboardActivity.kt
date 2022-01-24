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
import com.example.smartparkinglot.AppShareRefs
import com.example.smartparkinglot.BaseActivity
import com.example.smartparkinglot.R
import com.example.smartparkinglot.databinding.ActivityDashboardBinding
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class DashboardActivity : BaseActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    override fun initData() {
        val userId = AppShareRefs.getUserId(this)
        //call API


       CoroutineScope(Dispatchers.IO).launch{
           //JSON using JSONObject
           val jsonObject = JSONObject()
           with(jsonObject){
               put("id_user", userId)
           }
           // Convert JSONObject to String
           val jsonObjectString = jsonObject.toString()

           // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
           val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

           val result = RESTClient.createClient("")
               .create(APIService::class.java)
               .fetchMe(requestBody)
           withContext(Dispatchers.Main){
               if(result.isSuccessful){
                   //
               }else {

               }
           }
       }
    }

    override fun setAction() {
        TODO("Not yet implemented")
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