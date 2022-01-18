package com.example.smartparkinglot.custom

import android.app.Dialog
import android.widget.LinearLayout
import android.widget.TextView
import com.example.smartparkinglot.R

class ConfirmationDialog(var builder: Builder?): BaseDialog() {
    private var tvTitle: TextView? = null
    private var tvMessage: TextView? = null
    private var btnCancelTitle: TextView? = null
    private var btnOkTitle: TextView? = null
    private var btnOk: LinearLayout? = null
    private var btnCancel: LinearLayout? = null

    override fun createDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_confirmation)
        tvTitle = dialog.findViewById(R.id.tv_title)
        tvMessage = dialog.findViewById(R.id.tv_message)
        btnCancelTitle = dialog.findViewById(R.id.btn_cancel_title)
        btnOkTitle = dialog.findViewById(R.id.btn_ok_title)
        btnCancel = dialog.findViewById(R.id.btn_cancel)
        btnOk = dialog.findViewById(R.id.btn_ok)

        setup()
    }

    private fun setup() {
        builder?.let {
            it.title?.let { text ->
                tvTitle?.text = text
            }
            it.message?.let{ text ->
                tvMessage?.text = text
            }
            it.btnOKText?.let{ text ->
                btnOkTitle?.text = text
            }
            it.btnCancelText?.let{ text ->
                btnCancelTitle?.text = text
            }
        }
    }

    override fun clickEvents() {
        btnCancel?.setOnClickListener{
            dismiss()
        }
        btnOk?.setOnClickListener{
            builder?.listener?.invoke()
            dismiss()
        }
    }

    override fun release() {}

    data class Builder(
        var title: String? = "Confirmation",
        var message: String? = null,
        var btnOKText: String? = "OK",
        var btnCancelText: String? = "Cancel",
        var listener: (() -> Unit?)? = null) {
        fun title(title:String) = apply{this.title = title}
        fun message(message: String) = apply { this.message = message }
        fun btnOKText(text: String) = apply {this.btnOKText = text}
        fun btnCancelText(text: String) = apply { this.btnCancelText = text }
        fun listener(listener: (() -> Unit?)?) = apply { this.listener = listener }
        fun build() = ConfirmationDialog(this)
    }
}