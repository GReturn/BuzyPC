package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R

class BuildTrackerRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<io.buzypc.app.Data.BuildData.PCBuild>,
    var onViewCheckListButtonClicked: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.card_build_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val build = pcBuilds[position]
        val item = holder as ItemViewHolder
        item.tvName.text = build.name
        item.tvBudget.text = "PHP ${build.budget}"

        item.container.setOnClickListener {
            onViewCheckListButtonClicked()
        }
    }

    override fun getItemCount(): Int {
        return pcBuilds.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_pcName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)

        val container = itemView.findViewById<CardView>(R.id.build_card)
    }

}