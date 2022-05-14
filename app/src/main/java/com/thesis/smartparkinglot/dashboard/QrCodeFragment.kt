package com.thesis.smartparkinglot.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.thesis.smartparkinglot.R
import com.thesis.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.thesis.smartparkinglot.databinding.FragmentQrCodeBinding
import com.thesis.smartparkinglot.retrofit.RESTClient
import com.squareup.picasso.Picasso

class QrCodeFragment : Fragment() {
    lateinit var binding : FragmentQrCodeBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
//    var mainHandler: Handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_qr_code,container,false)
        requireActivity().run {
            userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        }
        binding.viewModel = userInfoViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfoViewModel.user.observe(viewLifecycleOwner) {
            val picasso = Picasso.get()
            picasso.isLoggingEnabled = true
            picasso.load(RESTClient.BASE_URL + it?.linkQr)
                .into(binding.qrCode)
        }
    }

}