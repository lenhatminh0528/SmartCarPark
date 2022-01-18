package com.example.smartparkinglot.custom

import android.app.Dialog
import com.example.smartparkinglot.R

class LoadingDialog : BaseDialog() {
    override fun createDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
    }

    override fun clickEvents() {}

    override fun release() {}
}