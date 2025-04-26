package io.buzypc.app.UI.Navigation.Fragments.Settings.Archive

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.saveBuildList
import io.buzypc.app.UI.Widget.DialogView.ArchiveBottomSheetDialog
import java.util.Calendar

class ArchiveListRecyclerViewAdapter (
    var context: Context,
    var pcBuilds: ArrayList<PCBuild>,
    private val archiveViewModel: ArchiveViewModel,
    private val listsInformationViewModel: ListsInformationViewModel,
    private val buildListChangedListener: OnBuildListChangedListener

): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ItemViewHolder(inflater.inflate(R.layout.card_build_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pcBuilds.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder ->{
                val build = pcBuilds[position]
                holder.tvName.text = build.name
                holder.tvBudget.text = "PHP %,.2f".format(build.budget)
                holder.btnAddToChecklist.isVisible = false

                val calendar = Calendar.getInstance().apply { time = build.createdAt }
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val year = calendar.get(Calendar.YEAR)
                val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                holder.tvCreatedAt.text = "Created: ${monthNames[month]} ${day}, ${year}"


                holder.itemView.setOnLongClickListener() {
                    val appSession = context.applicationContext as BuzyUserAppSession
                    val bottomSheet = ArchiveBottomSheetDialog(context, archiveViewModel, pcBuilds)
                    archiveViewModel.setBuild(build)

                    bottomSheet.show(
                        (holder.itemView.context as FragmentActivity).supportFragmentManager,
                        "ModalBottomSheet"
                    )

                    updateList(pcBuilds)
                    saveBuildList(context, pcBuilds)

                    val buildCount = appSession.buildList.count { !it.isDeleted && !it.isArchived}
                    val checklistCount = appSession.buildList.count { !it.isDeleted && !it.isArchived && it.isTracked }
                    listsInformationViewModel.setBuildCount(buildCount)
                    listsInformationViewModel.setChecklistItemCount(checklistCount)

                    buildListChangedListener.onBuildListChanged(
                        appSession.buildList.none { !it.isDeleted && it.isArchived}
                    )
                    true
                }

            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_buildName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.textView_createdAt)
        val imgBtnRemove: ImageButton = itemView.findViewById(R.id.imgBtn_removeItem)
        val btnViewSummary: MaterialButton = itemView.findViewById(R.id.btn_viewSummary)
        val btnAddToChecklist: ImageButton = itemView.findViewById(R.id.btn_addToChecklist)
        val buildCard: CardView = itemView.findViewById(R.id.build_card)
    }


    private fun updateList(newBuilds: List<PCBuild>) {
        pcBuilds = ArrayList(newBuilds.filter { !it.isDeleted && it.isArchived })
        notifyDataSetChanged()
    }

}