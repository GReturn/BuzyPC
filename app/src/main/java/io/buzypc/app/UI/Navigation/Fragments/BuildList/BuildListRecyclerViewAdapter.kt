package io.buzypc.app.UI.Navigation.Fragments.BuildList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.R

class BuildListRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<io.buzypc.app.Data.BuildData.PCBuild>,
    var onNavigateToNewBuild: () -> Unit
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
                holder.tvBudget.text = "PHP ${build.budget}"

                // 1) Click on the whole card → BuildSummaryActivity
                holder.container.setOnClickListener {

                    val appSession = (context.applicationContext as BuzyUserAppSession)
                    appSession.selectedBuildInBuildList = build

                    val intent = Intent(context, BuildSummaryActivity::class.java)
                    context.startActivity(intent)
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

        val container = itemView.findViewById<CardView>(R.id.build_card)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: ImageView = itemView.findViewById(R.id.iv_addCard)

        val container = itemView.findViewById<CardView>(R.id.card_addBuild)
    }
}
