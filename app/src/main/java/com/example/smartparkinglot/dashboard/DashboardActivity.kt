package com.example.smartparkinglot.dashboard

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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
import com.example.smartparkinglot.Result
import com.example.smartparkinglot.custom.AlertDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.databinding.ActivityDashboardBinding
import com.example.smartparkinglot.model.User
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import com.example.smartparkinglot.utils.NetworkUtils
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

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG, "onAvailable: network connecting")
            alertDialog.dismiss()
            getFirstData()
        }

    }
    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        setContentView(binding.root)
        setupNavigation()
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

    override fun initData() {
        //connect wifi
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }



    override fun setAction() {
        getFirstData()
    }

    private fun getFirstData() {
        //call API
        loadingDialog = showLoadingDialog()

        if(!NetworkUtils.isNetworkConnect(this)) {
            loadingDialog.dismiss()
            showErrorDialog("No network connection!")
        } else {
            val userId: String = intent.extras?.get("user_id").toString()
            CoroutineScope(Dispatchers.IO).launch {
                when (val result = userInfoViewModel.fetchData(userId)) {
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            loadingDialog.dismiss()
                        }
                    }
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            loadingDialog.dismiss()
                            showErrorDialog(result.exception.message ?: "Something went wrong!")
                        }
                    }
                }
            }
        }
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