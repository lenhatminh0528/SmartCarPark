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
import com.thesis.smartparkinglot.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {
    private lateinit var _binding: FragmentUserInfoBinding
    val binding get() = _binding
    private var userViewModel: UserInfoViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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