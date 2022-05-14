package com.thesis.smartparkinglot.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.Window
import androidx.fragment.app.DialogFragment

abstract class BaseDialog: DialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
//        super.setupDialog(dialog, style)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        createDialog(dialog)
        clickEvents()
    }

    abstract fun createDialog(dialog: Dialog)
    abstract fun clickEvents()
    abstract fun release()

}