package com.example.smartparkinglot.authentication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.smartparkinglot.AppShareRefs
import com.example.smartparkinglot.hideKeyboard
import com.example.smartparkinglot.R
import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.custom.ConfirmationDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.dashboard.DashboardActivity
import com.example.smartparkinglot.databinding.FragmentLoginBinding
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginFragment : Fragment() {
    private var mConfirmationDialog: ConfirmationDialog? = null
    private var loading: LoadingDialog? = null
    private lateinit var binding: FragmentLoginBinding
    private lateinit var rootActivity: AuthActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        rootActivity = activity as AuthActivity
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
        rootActivity.showBottomSheet()

        val userName = binding.username
        val password = binding.password

        val jsonObject = JSONObject()
        with(jsonObject){
            put("username", userName)
            put("password", password)
        }
        showLoading()
        // call api
        CoroutineScope(Dispatchers.IO).launch {

            val response = RESTClient.createClient("https://855d-116-110-40-48.ngrok.io")
                .create(APIService::class.java)
                .signIn(jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    loading?.dismiss()
//                    AppShareRefs.setUserId(this,response?.)
                    val intent = Intent(rootActivity, DashboardActivity::class.java)
                    startActivity(intent)
                }else {
                    loading?.dismiss()
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

        }
    }

    private fun showLoading(){
        loading = rootActivity.showLoadingDialog()
    }


    private fun showConfirmDialog(){
        mConfirmationDialog = rootActivity.showConfirmationDialog("Confirmation",
            "Do you want to log out?",
            "OK", "Cancel") {

        }
    }
}