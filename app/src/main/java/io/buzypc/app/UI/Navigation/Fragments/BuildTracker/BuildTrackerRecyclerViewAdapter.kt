package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.BuildList.BuildSummaryActivity
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.saveBuildList
import io.buzypc.app.UI.Widget.DialogView.CustomActionDialogView
import io.buzypc.app.UI.Widget.DialogView.DialogType
import java.util.Calendar

class BuildTrackerRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<PCBuild>,
    private val listsInformationViewModel: ListsInformationViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_checklist_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val build = pcBuilds[position]

        val item = holder as ItemViewHolder
        item.tvName.text = build.name
        item.tvBudget.text = "PHP %,.2f".format(build.budget)

        val initialProgress = build.getProgress().first
        val totalComponents = build.getProgress().second
        item.tvProgress.text = "Progress: ${initialProgress}/${totalComponents} components bought"

        val calendar = Calendar.getInstance().apply { time = build.createdAt }
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        item.tvCreatedAt.text = "Created: ${monthNames[month]} ${day}, ${year}"

        item.btnViewCheckList.setOnClickListener {
            val appSession = (context.applicationContext as BuzyUserAppSession)
            appSession.selectedBuildInBuildTracker = build

            val intent = Intent(context, TrackingBuildSummaryActivity::class.java)
            context.startActivity(intent)
        }
        item.btnViewSummary.setOnClickListener {
            val appSession = (context.applicationContext as BuzyUserAppSession)
            appSession.selectedBuildInBuildList = build

            val intent = Intent(context, BuildSummaryActivity::class.java)
            context.startActivity(intent)
        }
        item.imgBtnRemove.setOnClickListener {
            CustomActionDialogView(context, DialogType.DESTRUCTION)
                .setTitle("Remove From Checklist")
                .setDescription("Are you sure you want to remove this build from your checklist?")
                .setOnCancelClickListener {

                }
                .setOnConfirmClickListener {
                    build.isTracked = false
                    updateList(pcBuilds)
                    saveBuildList(context, pcBuilds)

                    val checklistCount = pcBuilds.count { !it.isDeleted && it.isTracked }
                    listsInformationViewModel.setChecklistItemCount(checklistCount)

                    Toast.makeText(context, "Build removed from checklist.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return pcBuilds.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_buildName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)
        val tvProgress: TextView = itemView.findViewById(R.id.textView_progress)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.textView_createdAt)

        val imgBtnRemove: ImageButton = itemView.findViewById(R.id.imgBtn_removeItem)
        val btnViewSummary: MaterialButton = itemView.findViewById(R.id.btn_viewSummary)
        val btnViewCheckList: MaterialButton = itemView.findViewById(R.id.btn_addToChecklist)
    }

    private fun updateList(newBuilds: List<PCBuild>) {
        pcBuilds = ArrayList(newBuilds.filter { !it.isDeleted && it.isTracked })
        notifyDataSetChanged()
    }
}