package io.buzypc.app.ui.navigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.data.pc.PCBuild
import io.buzypc.app.model.PCBuildRecyclerViewAdapter
import io.buzypc.app.ui.navigation.BottomNavigation
import io.buzypc.app.ui.utils.loadCurrentUserDetails

class BuildListFragment : Fragment() {
    val pcBuildList = ArrayList<PCBuild>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        setPCModelList()

        val tvEmptyList = view.findViewById<TextView>(R.id.tvEmptyList)
        if(pcBuildList.isEmpty()){
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        val adapter = PCBuildRecyclerViewAdapter(requireContext(), pcBuildList) {
            // we handle the click event here when user clicks on the plus button inside the buildlist fragment
            val activity = requireActivity() as BottomNavigation
            activity.handleNavigateToNewBuild()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
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