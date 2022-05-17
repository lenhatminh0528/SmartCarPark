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
    private lateinit var binding: FragmentLoginBinding
    private lateinit var rootActivity: AuthActivity
    private lateinit var viewModel : LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        rootActivity = activity as AuthActivity
        viewModel = ViewModelProvider(rootActivity).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAction(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAction(view:View) {
        view.setOnTouchListener{_, _ ->
            view.username.clearFocus()
            view.password.clearFocus()
            activity?.hideKeyboard(view)
            true
        }

        binding.textRegister.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterInfoFragment())
        }
    }
}