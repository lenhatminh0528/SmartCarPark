package com.example.smartparkinglot.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.smartparkinglot.R
import com.example.smartparkinglot.dashboard.viewmodel.UserInfoViewModel
import com.example.smartparkinglot.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {
    private val TAG = "UserInfoFragment"
    private lateinit var _binding: FragmentUserInfoBinding
    val binding get() = _binding
    var userViewModel: UserInfoViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info,container,false)
        userViewModel = requireActivity().run {
            ViewModelProvider(this).get(UserInfoViewModel::class.java)
        }
//        _binding.lifecycleOwner = this     Setting the fragment as the LifecycleOwner might cause memory leaks
        binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = userViewModel
        return _binding.root
    }

}