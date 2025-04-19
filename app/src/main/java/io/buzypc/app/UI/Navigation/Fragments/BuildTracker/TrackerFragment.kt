package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Utils.LayoutManagers.BuildTrackerListLayoutManager
import io.buzypc.app.UI.Utils.loadBuildList

class TrackerFragment : Fragment() {
    val pcBuildList = ArrayList<io.buzypc.app.Data.BuildData.PCBuild>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        val tvEmptyList = view.findViewById<TextView>(R.id.tvEmptyList)
        setPCModelList()

        if(pcBuildList.isEmpty()){
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        val adapter = BuildTrackerRecyclerViewAdapter(requireContext(), pcBuildList) {
            val activity = requireActivity() as BottomNavigationActivity
            activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = BuildTrackerListLayoutManager(requireContext(),1)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView_builds)
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

    private fun setPCModelList() {
        val buildList = loadBuildList(requireContext())
        val size = buildList.size
        for (i in size-1 downTo 0) {
            pcBuildList.add(buildList[i])
        }
    }
}