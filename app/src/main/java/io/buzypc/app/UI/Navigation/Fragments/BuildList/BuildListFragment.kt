package io.buzypc.app.UI.Navigation.Fragments.BuildList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Navigation.Fragments.Settings.Archive.ArchiveListActivity
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.LayoutManagers.BuildListLayoutManager

class BuildListFragment : Fragment() {
    private lateinit var app: BuzyUserAppSession
    private lateinit var pcBuildList: ArrayList<PCBuild>
    private lateinit var allBuilds: ArrayList<PCBuild>
    private lateinit var adapter: BuildListRecyclerViewAdapter
    private val listsInformationViewModel: ListsInformationViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        val emptyListMessage = view?.findViewById<LinearLayout>(R.id.layout_emptyList)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView_builds)

        app = context?.applicationContext as BuzyUserAppSession
        pcBuildList = app.buildList

        allBuilds = pcBuildList.filter{!it.isDeleted && !it.isArchived} as ArrayList<PCBuild>
        if(allBuilds.isEmpty()){
            emptyListMessage?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyListMessage?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }

        adapter = BuildListRecyclerViewAdapter(
            requireContext(),
            allBuilds,
            {
                // we handle the click event here when user clicks on the plus button inside the buildlist fragment
                val activity = requireActivity() as BottomNavigationActivity
                activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
            },
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    emptyListMessage?.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        )

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = BuildListLayoutManager(requireContext(),1)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app = requireActivity().application as BuzyUserAppSession
        pcBuildList = app.buildList

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleView_builds)
        val emptyListMessage = view.findViewById<LinearLayout>(R.id.layout_emptyList)
        val imageAddBuild = view.findViewById<ImageView>(R.id.image_addNewBuild)
        val imageButtonArchive = view.findViewById<ImageView>(R.id.imageButton_archiveList)

        val fab = view.findViewById<FloatingActionButton>(R.id.fabScrollToTop)
        fab.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        allBuilds = pcBuildList.filter {!it.isDeleted && !it.isArchived} as ArrayList<PCBuild>

        if(allBuilds.isEmpty()){
            emptyListMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
        else {
            emptyListMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        imageAddBuild.setOnClickListener {
            val activity = requireActivity() as BottomNavigationActivity
            activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
        }
        imageButtonArchive.setOnClickListener {
            startActivity(Intent(requireContext(), ArchiveListActivity::class.java))
        }

        adapter = BuildListRecyclerViewAdapter(
            requireContext(),
            allBuilds,
            {
            // we handle the click event here when user clicks on the plus button inside the buildlist fragment
                val activity = requireActivity() as BottomNavigationActivity
                activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
            },
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    emptyListMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = BuildListLayoutManager(requireContext(),1)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    // Scrolling down
                    if (fab.isShown) fab.hide()
                } else if (dy < 0) {
                    // Scrolling up
                    if (!fab.isShown && recyclerView.canScrollVertically(-1)) {
                        fab.show()
                    }
                }

                // Hide when at top
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.hide()
                }
            }
        })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val emptyListMessage = view?.findViewById<LinearLayout>(R.id.layout_emptyList)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView_builds)

        val allBuilds = app.buildList.filter { !it.isDeleted && !it.isArchived} as ArrayList<PCBuild>
        if(allBuilds.isEmpty()){
            emptyListMessage?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyListMessage?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }

        adapter = BuildListRecyclerViewAdapter(
            requireContext(),
            allBuilds,
            {
                // we handle the click event here when user clicks on the plus button inside the buildlist fragment
                val activity = requireActivity() as BottomNavigationActivity
                activity.handleNavigationToOtherFragments(R.id.newBuildFragment)
            },
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    emptyListMessage?.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        )

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = BuildListLayoutManager(requireContext(),1)


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
}