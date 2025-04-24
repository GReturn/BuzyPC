package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Utils.LayoutManagers.BuildTrackerListLayoutManager

class TrackerFragment : Fragment() {
    private lateinit var app: BuzyUserAppSession
    private lateinit var pcBuildList: ArrayList<PCBuild>
    private lateinit var trackedBuilds: ArrayList<PCBuild>
    private lateinit var adapter: BuildTrackerRecyclerViewAdapter
    private val listsInformationViewModel: ListsInformationViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        trackedBuilds.clear()
        trackedBuilds.addAll(app.buildList.filter { it.isTracked && !it.isDeleted })
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app = requireActivity().application as BuzyUserAppSession
        pcBuildList = app.buildList

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        val emptyListMessage = view.findViewById<LinearLayout>(R.id.layout_emptyList)

        trackedBuilds = pcBuildList.filter { it.isTracked && !it.isDeleted } as ArrayList<PCBuild>
        if(trackedBuilds.isEmpty()){
            emptyListMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
        else {
            emptyListMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        adapter = BuildTrackerRecyclerViewAdapter(
            requireContext(),
            trackedBuilds,
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    emptyListMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
                }
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = BuildTrackerListLayoutManager(requireContext(),1)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val emptyListMessage = view?.findViewById<LinearLayout>(R.id.layout_emptyList)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView_builds)

        val trackedBuilds = pcBuildList.filter { it.isTracked && !it.isDeleted } as ArrayList<PCBuild>
        if(trackedBuilds.isEmpty()){
            emptyListMessage?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyListMessage?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }

        recyclerView?.layoutAnimation = null
        recyclerView?.doOnLayout {
            (recyclerView.layoutManager as AnimatedGridLayoutManager).animateItemsIn()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }
}