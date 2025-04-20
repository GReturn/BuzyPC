package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.R
import java.util.Calendar

class BuildTrackerRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<io.buzypc.app.Data.BuildData.PCBuild>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_tracker_item, parent, false)
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
    }

    override fun getItemCount(): Int {
        return pcBuilds.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_buildName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)
        val tvProgress: TextView = itemView.findViewById(R.id.textView_progress)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.textView_createdAt)
        val btnViewCheckList: MaterialButton = itemView.findViewById(R.id.btn_viewChecklist)
    }

}