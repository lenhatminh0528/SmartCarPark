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
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import com.example.smartparkinglot.custom.LoadingDialog
import com.example.smartparkinglot.network.APIService
import com.example.smartparkinglot.network.RESTClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
import java.io.File


class RegisterInfoFragment : Fragment() {

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

    private lateinit var startForGalleryResult: ActivityResultLauncher<String>
    private lateinit var startForCameraResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)
        rootActivity = activity as AuthActivity

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
                var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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
        showLoading()
        if(isValidate()){
            callAPI()
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

    private fun callAPI() {
        val retrofit = RESTClient.createClient("https://855d-116-110-40-48.ngrok.io")
        // Create Service
        val service = retrofit.create(APIService::class.java)

        val userName = binding.edtUsername.text
        val password = binding.edtPassword.text
        val idCard = binding.edtCardId.text
        val carnum = binding.edtCarNumber.text
        val address= if(binding.edtAddress.text?.isNotEmpty() == true) binding.edtAddress.text else ""

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
            with(jsonObject){
                put("username", userName)
                put("password", password)
                put("address", address)
                put("idcard", idCard)
                put("carnum", carnum)
            }
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.signUp(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
//                        )
//                    )
                    findNavController().popBackStack()
                    loading?.dismiss()
                } else {
                    loading?.dismiss()
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

}