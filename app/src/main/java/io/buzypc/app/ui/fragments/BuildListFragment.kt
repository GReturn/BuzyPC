package io.buzypc.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.data.PCModel
import io.buzypc.app.model.PCBuildAdapter
import io.buzypc.app.ui.ProfileViewActivity

class BuildListFragment : Fragment() {
    val pcModelList = ArrayList<PCModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        setPCModelList()

        val tvEmptyList = view.findViewById<TextView>(R.id.tvEmptyList)
        if(pcModelList.isEmpty()){
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        }


        val adapter = PCBuildAdapter(requireContext(), pcModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_build_list, container, false)
    }

    private fun setPCModelList() {
        val pcModelNames = getResources().getStringArray(R.array.pc_builds)
        for (i in pcModelNames.indices) {
            pcModelList.add(PCModel(pcModelNames[i]))
        }
    }

}