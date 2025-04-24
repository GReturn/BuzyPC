package io.buzypc.app.UI.Widget.DialogView

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.buzypc.app.R

class CustomInfoDialogView(
    private val context: Context
) {

    private lateinit var dialogView: View
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var btnConfirm: MaterialButton

    private lateinit var dialog: Dialog

    init {
        initializeViews()
    }

    private fun initializeViews() {
        dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_info, null)

        titleTextView = dialogView.findViewById(R.id.textViewTitle)
        messageTextView = dialogView.findViewById(R.id.textView_dialogMessage)
        btnConfirm = dialogView.findViewById(R.id.btnConfirm)

        dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
            .setView(dialogView)
            .setCancelable(true)
            .create()
    }

    fun setTitle(title: String): CustomInfoDialogView {
        titleTextView.text = title
        return this
    }

    fun setDescription(message: String): CustomInfoDialogView {
        messageTextView.text = message
        return this
    }

    fun setOnConfirmClickListener(listener: () -> Unit): CustomInfoDialogView {
        btnConfirm.setOnClickListener {
            listener()
            dialog.dismiss()
        }
        return this
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}