package io.buzypc.app.UI.Navigation.Fragments.BuildList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.saveBuildList
import io.buzypc.app.UI.Widget.DialogView.CustomActionDialogView
import io.buzypc.app.UI.Widget.DialogView.DialogType
import java.util.Calendar

class BuildListRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<PCBuild>,
    var onNavigateToNewBuild: () -> Unit,
    private val listsInformationViewModel: ListsInformationViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ADD_BUILD = 0
    private val VIEW_TYPE_BUILD_ITEM = 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_ADD_BUILD else VIEW_TYPE_BUILD_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        return if (viewType == VIEW_TYPE_BUILD_ITEM) {
            val view = inflater.inflate(R.layout.card_build_item, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.card_add_build_button, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                // since position 0 is the add button, builds start at index 1
                val build = pcBuilds[position - 1]
                holder.tvName.text = build.name
                holder.tvBudget.text = "PHP %,.2f".format(build.budget)

                val calendar = Calendar.getInstance().apply { time = build.createdAt }
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val year = calendar.get(Calendar.YEAR)
                val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                holder.tvCreatedAt.text = "Created: ${monthNames[month]} ${day}, ${year}"

                if(build.isTracked){
                    holder.btnAddToChecklist.setImageResource(R.drawable.ic_heart_filled)
                }
                else holder.btnAddToChecklist.setImageResource(R.drawable.ic_heart_nofill)

                holder.imgBtnRemove.setOnClickListener {
                    CustomActionDialogView(context, DialogType.DESTRUCTION)
                        .setTitle("Remove Build")
                        .setDescription("Are you sure you want to delete this build? This will permanently remove the build from your list.")
                        .setOnCancelClickListener {

                        }
                        .setOnConfirmClickListener {
                            build.isDeleted = true
                            updateList(pcBuilds)

                            saveBuildList(context, pcBuilds)

                            val buildCount = pcBuilds.count { !it.isDeleted }
                            listsInformationViewModel.setBuildCount(buildCount)

                            Toast.makeText(context, "Build removed from checklist.", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                }

                holder.btnViewSummary.setOnClickListener {
                    val appSession = (context.applicationContext as BuzyUserAppSession)
                    appSession.selectedBuildInBuildList = build

                    val intent = Intent(context, BuildSummaryActivity::class.java)
                    context.startActivity(intent)
                }

                holder.btnAddToChecklist.setOnClickListener {

                    if(build.isTracked) {
                        CustomActionDialogView(context, DialogType.DESTRUCTION)
                            .setTitle("Remove From Checklist")
                            .setDescription("Are you sure you want to remove this build from your checklist?")
                            .setOnCancelClickListener { }
                            .setOnConfirmClickListener {
                                build.isTracked = false
                                holder.btnAddToChecklist.setImageResource(R.drawable.ic_heart_nofill)

                                saveBuildList(context, pcBuilds)

                                val checklistCount = pcBuilds.count { !it.isDeleted && it.isTracked }
                                listsInformationViewModel.setChecklistItemCount(checklistCount)

                                Toast.makeText(context, "Item removed from checklist.", Toast.LENGTH_SHORT).show()
                            }
                            .show()
                    }
                    else {
                        CustomActionDialogView(context, DialogType.CONFIRMATION)
                            .setTitle("Add to Checklist")
                            .setDescription("Are you sure you want to add this item to your checklist?")
                            .setOnCancelClickListener { }
                            .setOnConfirmClickListener {
                                build.isTracked = true
                                holder.btnAddToChecklist.setImageResource(R.drawable.ic_heart_filled)

                                saveBuildList(context, pcBuilds)

                                val checklistCount = pcBuilds.count { !it.isDeleted && it.isTracked }
                                listsInformationViewModel.setChecklistItemCount(checklistCount)

                                Toast.makeText(context, "Item added to checklist.", Toast.LENGTH_SHORT).show()
                            }
                            .show()
                    }
                }
            }

            is ImageViewHolder -> {
                holder.button.setOnClickListener {
                    onNavigateToNewBuild()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return pcBuilds.size + 1
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_buildName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.textView_createdAt)
        val imgBtnRemove: ImageButton = itemView.findViewById(R.id.imgBtn_removeItem)
        val btnViewSummary: MaterialButton = itemView.findViewById(R.id.btn_viewSummary)
        val btnAddToChecklist: ImageButton = itemView.findViewById(R.id.btn_addToChecklist)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: ImageView = itemView.findViewById(R.id.iv_addCard)
    }


    private fun updateList(newBuilds: List<PCBuild>) {
        pcBuilds = ArrayList(newBuilds.filter { !it.isDeleted })
        notifyDataSetChanged()
    }
}
