package io.buzypc.app.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivities
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.data.PCModel
import io.buzypc.app.ui.SettingsActivity
import io.buzypc.app.ui.fragments.NewBuildFragment

class PCBuildAdapter(var context: Context, var pcModels: ArrayList<PCModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_BUTTON = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < pcModels.size) VIEW_TYPE_ITEM else VIEW_TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(R.layout.activity_build_card, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.footer_button, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.textview.text = pcModels[position].pcName
        } else if (holder is ImageViewHolder) {
            holder.button.setOnClickListener { view ->
                // Cast context to AppCompatActivity to get the supportFragmentManager
                val activity = context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, NewBuildFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }


    override fun getItemCount(): Int {
        return pcModels.size + 1
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textview: TextView = itemView.findViewById(R.id.textView_pcName)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: ImageView = itemView.findViewById(R.id.footerButton)
    }
}
