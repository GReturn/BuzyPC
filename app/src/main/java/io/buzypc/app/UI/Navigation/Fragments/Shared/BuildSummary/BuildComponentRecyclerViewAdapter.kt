package io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.Components.CPUComponent
import io.buzypc.app.Data.BuildData.Components.GPUComponent
import io.buzypc.app.Data.BuildData.Components.MotherboardComponent
import io.buzypc.app.Data.BuildData.Components.PSUComponent
import io.buzypc.app.Data.BuildData.Components.RAMComponent
import io.buzypc.app.Data.BuildData.Components.StorageDeviceComponent
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities.BuyComponentActivity
import io.buzypc.app.UI.Utils.formatDecimalPriceToPesoCurrencyString

class BuildComponentRecyclerViewAdapter(
    private val context: Context,
    private val componentList: List<Component>
) : RecyclerView.Adapter<BuildComponentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_buildlist_cpu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val component = componentList[position]

        if(component is CPUComponent) {
            holder.tvComponentLabel.text = "CPU"
            holder.imgComponent.setImageResource(R.drawable.ic_build_component_cpu)
        }
        if(component is GPUComponent) {
            holder.tvComponentLabel.text = "GPU"
            holder.imgComponent.setImageResource(R.drawable.gpu)
        }
        if(component is MotherboardComponent) {
            holder.tvComponentLabel.text = "Motherboard"
            holder.imgComponent.setImageResource(R.drawable.motherboard_vector)
        }
        if(component is RAMComponent) {
            holder.tvComponentLabel.text = "RAM"
            holder.imgComponent.setImageResource(R.drawable.ram_vector)
        }
        if(component is StorageDeviceComponent) {
            holder.tvComponentLabel.text = "Storage"
            holder.imgComponent.setImageResource(R.drawable.storage_vector)
        }
        if(component is PSUComponent) {
            holder.tvComponentLabel.text = "PSU"
            holder.imgComponent.setImageResource(R.drawable.psu_vector)
        }

        holder.tvComponentName.text = component.name
        val price = formatDecimalPriceToPesoCurrencyString(component.price)
        holder.tvComponentPrice.text = "PHP ${price}"

        holder.btnSeeStores.setOnClickListener {
            val appSession = context.applicationContext as BuzyUserAppSession

            appSession.component = component
            val intent = Intent(context, BuyComponentActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return componentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvComponentLabel: TextView = itemView.findViewById(R.id.tvComponentLabel)
        val tvComponentName: TextView = itemView.findViewById(R.id.tvBuildComponentName)
        val tvComponentPrice: TextView = itemView.findViewById(R.id.tvComponentPrice)
        val imgComponent: ImageView = itemView.findViewById(R.id.imgComponentIcon)
        val btnSeeStores: Button = itemView.findViewById(R.id.btnSeeStores)
    }
}