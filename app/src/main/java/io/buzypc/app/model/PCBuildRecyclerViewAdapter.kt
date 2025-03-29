package io.buzypc.app.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.data.pc.PCBuild

class PCBuildRecyclerViewAdapter(
    var context: Context,
    var pcBuilds: ArrayList<PCBuild>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_BUILD_ITEM = 0
    private val VIEW_TYPE_ADD_BUILD = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < pcBuilds.size) VIEW_TYPE_BUILD_ITEM else VIEW_TYPE_ADD_BUILD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == VIEW_TYPE_BUILD_ITEM) {
            val view = inflater.inflate(R.layout.buildlist_build_card, parent, false)
            ItemViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.footer_button, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            // populate each item of PC build here with corresponding fields

            holder.tvName.text = pcBuilds[position].buildName
            holder.tvBudget.text = "PHP " + pcBuilds[position].buildBudget

            // TODO add onclick listener here

        } else if (holder is ImageViewHolder) {
            holder.button.setOnClickListener {
                // Cast context to AppCompatActivity to get the supportFragmentManager
                val activity = context as AppCompatActivity
                val navHostFragment = activity.supportFragmentManager
                                .findFragmentById(R.id.navController) as NavHostFragment
                val navController = navHostFragment.navController

                // Normally using NavController.navigate() erases the current fragment
                // In our context, we are in the 'My Builds' fragment, which is erased when
                // we use navigate(). To counter this, we add a setting to our navOptions,
                // that is we add setPopUp(navController.graph.startDestinationId, false)
                // setting `true` in the second argument  will remove the previous fragment
                navController.navigate(
                    R.id.newBuildFragment,
                    null,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .setPopUpTo(navController.graph.startDestinationId, false)
                        .build()
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return pcBuilds.size + 1
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.textView_pcName)
        val tvBudget: TextView = itemView.findViewById(R.id.textView_pcBudget)

    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: ImageView = itemView.findViewById(R.id.footerButton)
    }
}
