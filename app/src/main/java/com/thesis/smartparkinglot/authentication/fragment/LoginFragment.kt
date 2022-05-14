package com.thesis.smartparkinglot.authentication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thesis.smartparkinglot.AppShareRefs
import com.thesis.smartparkinglot.Result
import com.thesis.smartparkinglot.hideKeyboard
import com.thesis.smartparkinglot.authentication.AuthActivity
import com.thesis.smartparkinglot.authentication.viewmodel.LoginViewModel
import com.thesis.smartparkinglot.custom.AlertDialog
import com.thesis.smartparkinglot.custom.ConfirmationDialog
import com.thesis.smartparkinglot.custom.LoadingDialog
import com.thesis.smartparkinglot.dashboard.DashboardActivity
import com.thesis.smartparkinglot.databinding.FragmentLoginBinding
import com.thesis.smartparkinglot.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.*

class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"
    private var mConfirmationDialog: ConfirmationDialog? = null
    private var loading: LoadingDialog? = null
    private lateinit var binding: FragmentLoginBinding
    private lateinit var rootActivity: AuthActivity
    private lateinit var alertDialog: AlertDialog
    private lateinit var viewModel : LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        rootActivity = activity as AuthActivity
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAction(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAction(view:View) {

        view.setOnTouchListener(View.OnTouchListener{_, _ ->
            view.username.clearFocus()
            view.password.clearFocus()
            activity?.hideKeyboard(view)
            true
        })

        binding.btnSignIn.setOnClickListener {
            handleBtnLogin()
        }

        binding.textRegister.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterInfoFragment())
        }

    }

    private fun handleBtnLogin() {
        binding.username.clearFocus()
        binding.password.clearFocus()

        if (!isValidate()) return
        showLoading()
        if(!NetworkUtils.isNetworkConnect(requireContext())) {
            showErrorDialog("No network connection!")
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                when(val result = viewModel.callAPI()) {
                    is Result.Success -> {
                        loading?.dismiss()
                        result.data?.let { AppShareRefs.setUserId(rootActivity, it) }
                        val intent = Intent(rootActivity, DashboardActivity::class.java)
                        intent.putExtra("user_id", id)
                        startActivity(intent)
                    }
                    is Result.Error ->
                        result.exception.message?.let {
                            showErrorDialog(it)
                            loading?.dismiss()
                        }
                }
            }
        }
    }

    private fun isValidate() : Boolean {
        if(binding.username.text?.isEmpty() == true){
            binding.username.requestFocus()
            return false
        }
        if(binding.password.text?.isEmpty() == true){
            binding.password.requestFocus()
            return false
        }
        return true
    }

    private fun showLoading(){
        loading = rootActivity.showLoadingDialog()
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

        alertDialog.show(childFragmentManager, "ALERT")
    }

    private fun showSuccessDialog(message: String) {
        alertDialog = AlertDialog.Builder()
            .setSuccess(true)
            .title("Successful")
            .message(message)
            .onConfirm {
                alertDialog.dismiss()
                val intent = Intent(rootActivity, DashboardActivity::class.java)
                startActivity(intent)
            }
            .build()
        alertDialog.show(childFragmentManager, "ALERT")
    }

    private fun showConfirmDialog(){
        mConfirmationDialog = rootActivity.showConfirmationDialog("Confirmation",
            "Do you want to log out?",
            "OK", "Cancel") {
        }
    }
}