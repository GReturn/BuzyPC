package io.buzypc.app.UI.Widget.DialogView

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import io.buzypc.app.R

class CustomActionDialogView(
    private val context: Context,
    private val dialogType: DialogType
) {

    private lateinit var dialogView: View
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnConfirm: MaterialButton

    private lateinit var dialog: AlertDialog

    init {
        initializeViews()
    }

    private fun initializeViews() {
        dialogView = when (dialogType) {
            DialogType.DESTRUCTION -> LayoutInflater.from(context)
                .inflate(R.layout.dialog_destructive_action, null)
            DialogType.CONFIRMATION -> LayoutInflater.from(context)
                .inflate(R.layout.dialog_confirmation_action, null)
        }

        titleTextView = dialogView.findViewById(R.id.textViewTitle)
        messageTextView = dialogView.findViewById(R.id.textView_dialogMessage)
        btnCancel = dialogView.findViewById(R.id.btnCancel)
        btnConfirm = dialogView.findViewById(R.id.btnConfirm)

        dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .create()
    }

    fun setTitle(title: String): CustomActionDialogView {
        titleTextView.text = title
        return this
    }

    fun setDescription(message: String): CustomActionDialogView {
        messageTextView.text = message
        return this
    }

    fun setOnConfirmClickListener(listener: () -> Unit): CustomActionDialogView {
        btnConfirm.setOnClickListener {
            listener()
            dialog.dismiss()
        }
        return this
    }

    fun setOnCancelClickListener(listener: () -> Unit): CustomActionDialogView {
        btnCancel.setOnClickListener {
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