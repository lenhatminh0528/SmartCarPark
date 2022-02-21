package com.example.smartparkinglot.authentication.fragment

import android.annotation.SuppressLint
import android.app.Activity
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
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher

import androidx.lifecycle.ViewModelProvider
import com.example.smartparkinglot.Result
import com.example.smartparkinglot.authentication.viewmodel.RegistererInfoViewModel
import com.example.smartparkinglot.custom.AlertDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult


class RegisterInfoFragment : Fragment() {
    private val TAG = "RegisterInfoFragment"

    companion object {
        private const val REQUEST_CAMERA = 2
        private const val REQUEST_FILE = 1
        private const val PICK_IMAGE = 1
        private const val PERMISSION_REQUEST = 10
    }
    private var loading: LoadingDialog? = null
    private var bottomSheet: BottomSheetDialog? = null
    private lateinit var rootActivity: AuthActivity
    private lateinit var binding: FragmentRegisterInfoBinding
    private lateinit var viewModel : RegistererInfoViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var startForGalleryResult: ActivityResultLauncher<String>
    private lateinit var startForCameraResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RegistererInfoViewModel::class.java)
        rootActivity = activity as AuthActivity
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForGalleryResult =
            registerForActivityResult(ActivityResultContracts.GetContent())
            {
                binding.image.setImageURI(it)
                binding.image.visibility = View.VISIBLE
                binding.textImg.visibility = View.GONE
            }

        startForCameraResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode == Activity.RESULT_OK){
                val bitmap = it.data?.extras?.get("data") as Bitmap
                binding.image.setImageBitmap(bitmap)
                binding.image.visibility = View.VISIBLE
                binding.textImg.visibility = View.GONE
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startForCameraResult.launch(cameraIntent)
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
                startForGalleryResult.launch("image/*")
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
            clearFocus()
            activity?.hideKeyboard(view)
            true
        })

        binding.carImg.setOnClickListener {
            bottomSheet!!.show()
        }

        binding.btnRegister.setOnClickListener{
            handleRegister()
        }
    }

    private fun clearFocus() {
        binding.edtPassword.clearFocus()
        binding.edtUsername.clearFocus()
        binding.edtCarNumber.clearFocus()
        binding.edtCardId.clearFocus()
        binding.edtAddress.clearFocus()
    }

    private fun handleRegister() {
        clearFocus()
        if(isValidate()){
            showLoading()
            if(!NetworkUtils.isNetworkConnect(requireContext())){
                loading?.dismiss()
                showErrorDialog("No network connection!")
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    when(val result = viewModel.callAPI()){
                        is Result.Success ->
                            showSuccessDialog("Register ${viewModel.username.value} successful!")
                        is Result.Error ->
                            result.exception.message?.let { showErrorDialog(it) }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        loading = rootActivity.showLoadingDialog()
    }

    private fun isValidate() : Boolean {
        if(binding.edtUsername.text?.isEmpty() == true){
            binding.edtUsername.requestFocus()
            return false
        }
        if(binding.edtPassword.text?.isEmpty() == true){
            binding.edtPassword.requestFocus()
            return false
        }

        if(binding.edtCardId.text?.isEmpty() == true){
            binding.edtCardId.requestFocus()
            return false
        }

        if(binding.edtCarNumber.text?.isEmpty() == true){
            binding.edtCarNumber.requestFocus()
            return false
        }
        return true
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
                findNavController().popBackStack()
                alertDialog.dismiss()
            }
            .build()
        alertDialog.show(childFragmentManager, "ALERT")
    }

}