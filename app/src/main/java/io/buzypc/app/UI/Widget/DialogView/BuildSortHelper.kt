package io.buzypc.app.UI.Widget.DialogView

import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R

object BuildSortHelper {

    fun showSortDialogWithLayout(
        context: Context,
        builds: MutableList<PCBuild>,
        onSorted: () -> Unit
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_sort, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup_sortOptions)

        val btnApply = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnConfirm)
        val btnCancel = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancel)

        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .create()

        btnApply.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.radio_budget_low_high -> builds.sortBy { it.budget }
                R.id.radio_budget_high_low -> builds.sortByDescending { it.budget }
                R.id.radio_name_az -> builds.sortBy { it.name.lowercase() }
                R.id.radio_name_za -> builds.sortByDescending { it.name.lowercase() }
            }
            onSorted()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}