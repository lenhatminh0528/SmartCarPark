package com.example.smartparkinglot.dashboard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smartparkinglot.R
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.databinding.FragmentQrCodeBinding
import com.example.smartparkinglot.network.RESTClient
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.InputStream
import java.net.URL

class QrCodeFragment : Fragment() {
    private val TAG = "QrCodeFragment"
    lateinit var binding : FragmentQrCodeBinding
    lateinit var userInfoViewModel: UserInfoViewModel
    var mainHandler: Handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_qr_code,container,false)
        requireActivity().run {
            userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        }
        binding.viewModel = userInfoViewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfoViewModel.user.observe(viewLifecycleOwner, {
            var picasso = Picasso.get()
            picasso.isLoggingEnabled = true
            picasso.load(RESTClient.BASE_URL + it?.linkQr)
                .into(binding.qrCode)
        })
    }

}