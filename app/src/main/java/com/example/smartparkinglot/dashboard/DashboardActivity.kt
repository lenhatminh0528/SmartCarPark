package com.example.smartparkinglot.dashboard

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.smartparkinglot.BaseActivity
import com.example.smartparkinglot.R
import com.example.smartparkinglot.custom.AlertDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.dashboard.viewmodel.ViewModelFactory
import com.example.smartparkinglot.databinding.ActivityDashboardBinding
import com.example.smartparkinglot.repository.CarParkRepository
import com.example.smartparkinglot.retrofit.RESTClient
import com.example.smartparkinglot.room.UserRoomDatabase
import com.example.smartparkinglot.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.*

class DashboardActivity : BaseActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private var alertDialog: AlertDialog? = null
    private lateinit var userInfoViewModel: UserInfoViewModel
    private var loadingDialog : LoadingDialog? = null
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    override fun bindingView() {
        supportActionBar?.hide()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val factory = ViewModelFactory(repository = CarParkRepository(UserRoomDatabase.getInstance(this), RESTClient.getApi()))
        userInfoViewModel = ViewModelProvider(this, factory).get(UserInfoViewModel::class.java)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun showErrorDialog(message: String){
        alertDialog = AlertDialog.Builder()
            .setSuccess(false)
            .title("Alert")
            .message(message)
            .onConfirm {
                alertDialog?.dismiss()
            }
            .build()

        alertDialog?.show(supportFragmentManager, "ALERT")
    }

    override fun initData() {

    }

    private fun setupWifiConnection() {
        //connect wifi
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                alertDialog?.dismiss()
                getFirstData()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                showErrorDialog("No network connection!")
            }
        }
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun setAction() {

        userInfoViewModel.errorMessage.observe(this) {
            if (it != null) {
                loadingDialog?.dismiss()
                showErrorDialog(it)
            } else {
                loadingDialog?.dismiss()
            }
        }

        setupWifiConnection()
    }

    private fun getFirstData() {
        //call API
        val userId: String = intent.extras?.get("user_id").toString()
        loadingDialog = showLoadingDialog()

        if(!NetworkUtils.isNetworkConnect(this)) {
//            loadingDialog?.dismiss()
//            showErrorDialog("No network connection!")
            userInfoViewModel.errorMessage.value = "No network connection!"
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                userInfoViewModel.fetchUserInfo(userId)
            }
            
//
//            CoroutineScope(Dispatchers.IO).launch {
//                when (val result = userInfoViewModel.fetchData(userId)) {
//                    is Result.Success -> {
//                        withContext(Dispatchers.Main) {
//                            loadingDialog?.dismiss()
//                        }
//                    }
//                    is Result.Error -> {
//                        withContext(Dispatchers.Main) {
//                            loadingDialog?.dismiss()
//                            showErrorDialog(result.exception.message ?: "Something went wrong!")
//                        }
//                    }
//                }
//            }

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