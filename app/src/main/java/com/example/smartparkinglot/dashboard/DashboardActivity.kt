package com.example.smartparkinglot.dashboard

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.smartparkinglot.BaseActivity
import com.example.smartparkinglot.MyApplication
import com.example.smartparkinglot.R
import com.example.smartparkinglot.Result
import com.example.smartparkinglot.custom.AlertDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.dashboard.viewmodel.ViewModelFactory
import com.example.smartparkinglot.databinding.ActivityDashboardBinding
import com.example.smartparkinglot.repository.CarParkRepository
import com.example.smartparkinglot.retrofit.APIService
import com.example.smartparkinglot.retrofit.RESTClient
import com.example.smartparkinglot.room.UserRoomDatabase
import com.example.smartparkinglot.utils.NetworkUtils
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {
    private val TAG = "DashboardActivity"
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private var alertDialog: AlertDialog? = null
    lateinit var userInfoViewModel: UserInfoViewModel
//    @Inject
//    lateinit var userInfoViewModel: UserInfoViewModel

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
        AndroidInjection.inject(this)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        userInfoViewModel = ViewModelProvider(this, viewModelFactory).get(UserInfoViewModel::class.java)
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
                Log.d(TAG, "onAvailable: network connecting")
                alertDialog?.dismiss()
                getFirstData()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(TAG, "onLost: ")
                showErrorDialog("No network connection!")
            }
        }
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun setAction() {

        userInfoViewModel.errorMessage.observe(this, {
            loadingDialog?.dismiss()
            if (it != null) {
                showErrorDialog(it)
            }
        })

        setupWifiConnection()
    }

    private fun getFirstData() {
        //call API
        val userId: String = intent.extras?.get("user_id").toString()
        loadingDialog = showLoadingDialog()

        if(!NetworkUtils.isNetworkConnect(this)) {
            userInfoViewModel.errorMessage.value = "No network connection!"
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                userInfoViewModel.fetchUserInfo(userId)
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