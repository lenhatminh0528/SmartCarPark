package com.example.smartparkinglot.authentication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.smartparkinglot.hideKeyboard
import com.example.smartparkinglot.R
import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.custom.ConfirmationDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.view.*

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
        showLoading()
        // call api
        //show loader
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