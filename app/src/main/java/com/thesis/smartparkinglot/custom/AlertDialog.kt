package com.thesis.smartparkinglot.custom

import android.app.Dialog
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.thesis.smartparkinglot.R

class AlertDialog(var builder: Builder) : BaseDialog() {
    private var title: TextView? = null
    private var message: TextView? = null
    private var imgView: ImageView? = null
    private var btnConfirm: LinearLayout? = null
    override fun createDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_alert)
        title = dialog.findViewById(R.id.dialog_title)
        message = dialog.findViewById(R.id.dialog_message)
        imgView = dialog.findViewById(R.id.imgTitle)
        btnConfirm = dialog.findViewById(R.id.btn_ok)
        setup()
    }

    private fun setup() {
        builder.let {
            this.title?.text = it.title
            this.message?.text = it.message
            if(it.isSuccess){
                imgView?.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_success, null))
                imgView?.setColorFilter(ResourcesCompat.getColor(resources,R.color.green_mint,null))
                title?.setTextColor(Color.GREEN)
            }else {
                imgView?.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_alert, null))
                imgView?.setColorFilter(ResourcesCompat.getColor(resources,R.color.red, null))
                title?.setTextColor(Color.RED)

            }
        }
    }

    override fun clickEvents() {
        btnConfirm?.setOnClickListener{
            builder.listener?.invoke()
        }
    }

    override fun release() {

    }

    data class Builder(
        var title:String = "",
        var message:String = "",
        var isSuccess: Boolean = true, var listener: (() -> Unit?)? = null) {
        fun title(title:String) =  apply { this.title = title }
        fun message(message: String) = apply { this.message = message }
        fun setSuccess(isSuccess: Boolean) = apply { this.isSuccess = isSuccess }
        fun onConfirm(listener: () -> Unit?) = apply { this.listener = listener }
        fun build() = AlertDialog(this)
    }
}