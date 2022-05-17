package com.thesis.smartparkinglot.authentication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thesis.smartparkinglot.R
import com.thesis.smartparkinglot.custom.AlertDialog
import com.thesis.smartparkinglot.custom.ConfirmationDialog
import com.thesis.smartparkinglot.custom.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thesis.smartparkinglot.AppShareRefs
import com.thesis.smartparkinglot.authentication.viewmodel.LoginViewModel
import com.thesis.smartparkinglot.authentication.viewmodel.StateType
import com.thesis.smartparkinglot.authentication.viewmodel.StateView
import com.thesis.smartparkinglot.dashboard.DashboardActivity

abstract class AccountActivity: AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    var loadingDialog: LoadingDialog? = null
    var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView()
        initData()
        setAction()
        registerEvent()
    }

    abstract fun bindingView()
    abstract fun initData()
    abstract fun setAction()

    private fun registerEvent() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.apiState.observe(this) { state ->
            Log.d("SmartCarPark", "registerEvent: state: ${state.type} ${state.message}")
            when (state) {
                is StateView.Loading -> {
                    loadingDialog = showLoadingDialog()
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
                is StateView.Success -> {
                    loadingDialog?.dismiss()
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    if (state.type == StateType.SIGN_IN) {
                        AppShareRefs.setUserId(this, state.data.toString())
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("user_id", state.data.toString())
                        startActivity(intent)
                    }
                    if (state.type == StateType.SIGN_UP) {
//                        supportFragmentManager.beginTransaction().replace(R.id.loginFragment, RegisterInfoFragment)
                        showSuccessDialog(state.message.toString()) {
                            alertDialog?.dismiss()
                            supportFragmentManager.popBackStack()
                        }
                    }
                }

                is StateView.Error -> {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    loadingDialog?.dismiss()
                    showErrorDialog(state.message!!)
                    if (state.type == StateType.SIGN_UP) {
                        supportFragmentManager.popBackStack()
                    }

                }
                else -> {}
            }
        }
    }

    fun showConfirmationDialog(title:String, message: String,btnOKText: String, btnCancelText: String, listener:(() -> Unit)): ConfirmationDialog{
        val builder = ConfirmationDialog.Builder()
            .title(title)
            .message(message)
            .btnOKText(btnOKText)
            .btnCancelText(btnCancelText)
            .listener(listener)
            .build()
        builder.show(supportFragmentManager, "CONFIRMATION")
        return builder
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

    private fun showSuccessDialog(message: String, callBack: () -> Unit) {
        alertDialog = AlertDialog.Builder()
            .setSuccess(true)
            .title("Successful")
            .message(message)
            .onConfirm {
                callBack.invoke()
            }
            .build()
        alertDialog?.show(supportFragmentManager, "ALERT")
    }

    fun showLoadingDialog(): LoadingDialog{
        val dialog = LoadingDialog()
        dialog.show(supportFragmentManager, "LOADING")
        return dialog
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun showBottomSheet(): BottomSheetDialog{
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.dialog_bottom_sheet)
        return bottomSheet
    }

    fun checkPermission(context: Context, permissionArray: Array<String>): Boolean{
        var allSuccess = true
        for (i in permissionArray.indices){
            if(checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED){
                allSuccess = false
            }
        }
        return allSuccess
    }
}