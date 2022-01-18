package com.example.smartparkinglot.authentication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.smartparkinglot.R
import com.example.smartparkinglot.authentication.AuthActivity
import com.example.smartparkinglot.databinding.FragmentRegisterInfoBinding
import com.example.smartparkinglot.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_register_info.view.*
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider

//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
import java.io.File


class RegisterInfoFragment : Fragment() {

    companion object {
        private const val REQUEST_CAMERA = 2
        private const val REQUEST_FILE = 1
        private const val PICK_IMAGE = 1
        private const val PERMISSION_REQUEST = 10
    }

    private var bottomSheet: BottomSheetDialog? = null
    private lateinit var rootActivity: AuthActivity
    private lateinit var binding: FragmentRegisterInfoBinding
    private lateinit var startForResult: ActivityResultLauncher<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)
        rootActivity = activity as AuthActivity

    startForResult =
        rootActivity.registerForActivityResult(ActivityResultContracts.GetContent())
        {
            binding.image.setImageURI(it)
            binding.image.visibility = View.VISIBLE
            binding.textImg.visibility = View.GONE
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.setOnTouchListener(View.OnTouchListener{view, _ ->
//            view.edt_username.clearFocus()
//            view.edt_address.clearFocus()
//            view.edt_car_number.clearFocus()
//            view.edt_card_id.clearFocus()
//            activity?.hideKeyboard(view)
//             true
//        })
        setupBottomSheet()
        setupAction()
    }

    private fun setupBottomSheet() {

        bottomSheet = rootActivity.showBottomSheet()
        val btnCamera = bottomSheet!!.findViewById<LinearLayout>(R.id.btn_camera)
        val btnGallery = bottomSheet!!.findViewById<LinearLayout>(R.id.btn_gallery)

        btnCamera?.setOnClickListener {
            Log.d("TAG", "camera")
            bottomSheet!!.dismiss()
            if (rootActivity.checkPermission(
                    requireContext(),
                    arrayOf(android.Manifest.permission.CAMERA)
                )
            ) {
                //request camera
            } else {
                ActivityCompat.requestPermissions(
                    rootActivity,
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CAMERA
                )
            }
        }

        btnGallery?.setOnClickListener {
            Log.d("TAG", "Gallery")
            if (rootActivity.checkPermission(
                    requireContext(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            ) {
                startForResult.launch("image/*")
                bottomSheet!!.dismiss()
            } else {
                ActivityCompat.requestPermissions(
                    rootActivity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_FILE
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility", "QueryPermissionsNeeded")
    private fun setupAction() {

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.root.setOnTouchListener(View.OnTouchListener { view, _ ->
            view.edt_username.clearFocus()
            view.edt_address.clearFocus()
            view.edt_car_number.clearFocus()
            view.edt_card_id.clearFocus()
            activity?.hideKeyboard(view)
            true
        })

        binding.carImg.setOnClickListener {
            bottomSheet!!.show()
        }

    }

}