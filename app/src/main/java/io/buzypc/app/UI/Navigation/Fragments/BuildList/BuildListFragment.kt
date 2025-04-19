package io.buzypc.app.UI.Navigation.Fragments.BuildList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.Data.pc.PCBuild
import io.buzypc.app.model.BuildListRecyclerViewAdapter
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Utils.LayoutManagers.BuildListLayoutManager
import io.buzypc.app.UI.Utils.LayoutManagers.BuildTrackerListLayoutManager
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

class BuildListFragment : Fragment() {
    val pcBuildList = ArrayList<PCBuild>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        val tvEmptyList = view.findViewById<TextView>(R.id.tvEmptyList)
        setPCModelList()

        if(pcBuildList.isEmpty()){
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        val adapter = BuildListRecyclerViewAdapter(requireContext(), pcBuildList) {
            // we handle the click event here when user clicks on the plus button inside the buildlist fragment
            val activity = requireActivity() as BottomNavigationActivity
            activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = BuildListLayoutManager(requireContext(),1)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView_builds)
        recyclerView?.layoutAnimation = null
        recyclerView?.doOnLayout {
            (recyclerView.layoutManager as AnimatedGridLayoutManager).animateItemsIn()
        }
    }

    // - LayoutInflater converts xml file into a View object that the fragment displays
    // - 'container' is the parent view where this fragment’s UI will be placed, passing false as the third parameter because the system
    //    will handle attaching the fragment to the container automatically.
    // - savedInstanceState contains any previously saved state, restores them upon recreating (updating) the fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_build_list, container, false)
    }

    private fun setPCModelList() {
        val userDetails = loadCurrentUserDetails(requireContext())
        userDetails.retrieveBuilds()

        val nameList = userDetails.buildNameList
        val budgetList = userDetails.buildBudgetList
        val size = nameList.size
        for (i in size-1 downTo 0) {
            pcBuildList.add(PCBuild(nameList[i], budgetList[i]))
        }
    }

}