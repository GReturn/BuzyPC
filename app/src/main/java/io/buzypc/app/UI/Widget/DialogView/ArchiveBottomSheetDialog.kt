package io.buzypc.app.UI.Widget.DialogView

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Settings.Archive.ArchiveViewModel
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Utils.saveBuildList

class ArchiveBottomSheetDialog(
    private val context: Context,
    private val archiveViewModel: ArchiveViewModel,
    private var pcBuilds: ArrayList<PCBuild>,
    private val buildListChangedListener: OnBuildListChangedListener

) : BottomSheetDialogFragment() {
    private val appSession = context.applicationContext as BuzyUserAppSession

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(
            R.layout.bottom_sheet_layout,
            container, false
        )

        val btnRestore = v.findViewById<TextView>(R.id.tvRestore)
        val btnDelete = v.findViewById<TextView>(R.id.tvDelete)
        val pcBuild = archiveViewModel.build


        btnRestore.setOnClickListener() {
            CustomActionDialogView(context, DialogType.CONFIRMATION)
                .setTitle("Restore Build")
                .setDescription("This build will be restored to your build list.")
                .setOnCancelClickListener { }
                .setOnConfirmClickListener {
                    pcBuild?.isArchived = false
                    pcBuilds = ArrayList(pcBuilds.filter { !it.isDeleted && it.isArchived } as ArrayList<PCBuild>)
                    saveBuildList(context, appSession.buildList)
                    // Added !it.isArchived to filter out archived builds
                    buildListChangedListener.onBuildListChanged(appSession.buildList.none { !it.isDeleted && it.isArchived }
                    )
                    Toast.makeText(context, "Build restored.", Toast.LENGTH_SHORT).show()
                    (context as? Activity)?.setResult(Activity.RESULT_OK)
                    (context as? Activity)?.finish()
                    dismiss()
                }
                .show()
        }

        btnDelete.setOnClickListener {
            CustomActionDialogView(requireContext(), DialogType.DESTRUCTION)
                .setTitle("Delete Build")
                .setDescription("Are you sure you want to delete this build? This action cannot be undone.")
                .setOnCancelClickListener { }
                .setOnConfirmClickListener {
                    pcBuild?.isDeleted = true
                    pcBuilds = ArrayList(pcBuilds.filter { it.isDeleted} as ArrayList<PCBuild>)
                    saveBuildList(context, appSession.buildList)

                    Toast.makeText(context, "Build deleted.", Toast.LENGTH_SHORT).show()
                    (context as? Activity)?.setResult(Activity.RESULT_OK)
                    (context as? Activity)?.finish()
                    dismiss()
                }
                .show()
        }
        return v
    }
}