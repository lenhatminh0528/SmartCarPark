package com.example.smartparkinglot.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
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
import com.example.smartparkinglot.custom.AlertDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.databinding.ActivityDashboardBinding
import com.example.smartparkinglot.model.User
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class DashboardActivity : BaseActivity() {
    private val TAG = "DashboardActivity"
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var loadingDialog : LoadingDialog
    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        setContentView(binding.root)
        setupNavigation()
    }

    override fun initData() {
        //call API
        loadingDialog = showLoadingDialog()
        val userId: String = intent.extras?.get("user_id").toString()

       CoroutineScope(Dispatchers.IO).launch{
           //JSON using JSONObject
           val jsonObject = JSONObject()
           with(jsonObject){
               put("col", "user")
               put("id", userId)
           }

           val response = RESTClient.createClient()
               .create(APIService::class.java)
               .showDB("user", userId.substring(1, userId.length - 1))

           withContext(Dispatchers.Main){
               if(response.isSuccessful){
                   loadingDialog.dismiss()
                   val gson = GsonBuilder().setPrettyPrinting().create()

                   val prettyJson = gson.fromJson(
                       JsonParser.parseString(
                       response.body()
                           ?.string()), JsonObject::class.java)

                   val status: String = prettyJson.get("status").toString()

                   if(status.contains("success")){
                       val data = gson.fromJson(prettyJson.get("data"),User::class.java)
                       userInfoViewModel.user.postValue(data)
                   }else {
                       showErrorDialog("Cannot find user!")
                   }
               }else {
                   loadingDialog.dismiss()
                   showErrorDialog("Unknow error!")
               }
           }
       }

        userInfoViewModel.user.observe(this, {

        })
    }

    private fun showErrorDialog(message: String){
        alertDialog = AlertDialog.Builder()
            .setSuccess(false)
            .title("Alert")
            .message(message)
            .onConfirm {
                alertDialog.dismiss()
            }
            .build()

        alertDialog.show(supportFragmentManager, "ALERT")
    }

    override fun setAction() {
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