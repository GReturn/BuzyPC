package io.buzypc.app.UI.Navigation.Fragments.BuildList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.LayoutManagers.BuildListLayoutManager
import io.buzypc.app.UI.Utils.loadBuildList

class BuildListFragment : Fragment() {
    val pcBuildList = ArrayList<PCBuild>()
    private val listsInformationViewModel: ListsInformationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        val tvEmptyList = view.findViewById<TextView>(R.id.tvEmptyList)

        setPCModelList()

        if(pcBuildList.isEmpty()){
            tvEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        val adapter = BuildListRecyclerViewAdapter(
            requireContext(),
            pcBuildList,
            {
            // we handle the click event here when user clicks on the plus button inside the buildlist fragment
            val activity = requireActivity() as BottomNavigationActivity
            activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
        },
            listsInformationViewModel
        )


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_build_list, container, false)
    }

    private fun setPCModelList() {
        val buildList = loadBuildList(requireContext())

        val size = buildList.size
        for (i in size-1 downTo 0) {
            if(!buildList[i].isDeleted) {
                pcBuildList.add(buildList[i])
            }
        }
    }
}