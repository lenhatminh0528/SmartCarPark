package com.example.smartparkinglot

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.smartparkinglot.custom.ConfirmationDialog
import com.example.smartparkinglot.custom.LoadingDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.jar.Manifest

abstract class BaseActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView()
        initData()
        setAction()
    }

    abstract fun bindingView()
    abstract fun initData()
    abstract fun setAction()

    fun showConfirmationDialog(title:String, message: String,btnOKText: String, btnCancelText: String, listener:(() -> Unit)): ConfirmationDialog{
        val builder = ConfirmationDialog.Builder()
            .title(title)
            .message(message)
            .btnOKText(btnOKText)
            .btnCancelText(btnCancelText)
            .listener(listener)
            .build()
        builder.show(supportFragmentManager, "CONFIRMATION")
        return builder
    }

    fun showLoadingDialog(): LoadingDialog{
        val dialog = LoadingDialog()
        dialog.show(supportFragmentManager, "LOADING")
        return dialog
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun showBottomSheet(): BottomSheetDialog{
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.dialog_bottom_sheet)
//        val btnCamera = bottomSheet.findViewById<LinearLayout>(R.id.btn_camera)
//        val btnGallery = bottomSheet.findViewById<LinearLayout>(R.id.btn_gallery)
//        btnCamera?.setOnClickListener{
//            Log.d("TAG", "camera")
//            if(checkPermission(this,arrayOf(android.Manifest.permission.CAMERA))){
//                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                if (takePictureIntent.resolveActivity(packageManager)!= null){
//                    startActivityForResult(takePictureIntent, REQUEST_CAMERA)
//                }
//                bottomSheet.dismiss()
//            }else {
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA)
//            }
//        }
//
//        btnGallery?.setOnClickListener{
//            Log.d("TAG", "Gallery")
//            if(checkPermission(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))){
//                val gallery1 = Intent()
//                gallery1.type = "image/*"
//                gallery1.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(gallery1, "choose image"), PICK_IMAGE)
//                bottomSheet.dismiss()
//            }else {
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    REQUEST_FILE)
//            }
//        }
//        bottomSheet.show()
        return bottomSheet
    }

    fun checkPermission(context: Context, permissionArray: Array<String>): Boolean{
        var allSuccess = true
        for (i in permissionArray.indices){
            if(checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED){
                allSuccess = false
            }
        }
        return allSuccess
    }
}