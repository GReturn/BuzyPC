package io.buzypc.app.UI.Navigation.Fragments.Settings.Archive

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.LayoutManagers.AnimatedGridLayoutManager
import io.buzypc.app.UI.Utils.LayoutManagers.ArchivedListLayoutManager

class ArchiveListActivity : AppCompatActivity() {
    private lateinit var app: BuzyUserAppSession
    private lateinit var pcBuildList: ArrayList<PCBuild>
    private val listsInformationViewModel: ListsInformationViewModel by viewModels()
    private val archiveViewModel: ArchiveViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_archive_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        app = application as BuzyUserAppSession

        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_archivedBuilds)
        val lLArchiveEmptyList = findViewById<LinearLayout>(R.id.lLEmptyArchiveList)
        val imgBtnBack = findViewById<ImageView>(R.id.image_back)

        val fab = findViewById<FloatingActionButton>(R.id.fabScrollToTop)
        fab.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        imgBtnBack.setOnClickListener(){
            finish()
        }

        pcBuildList = app.buildList

        val archivedBuilds = pcBuildList.filter{!it.isDeleted && it.isArchived} as ArrayList<PCBuild>
        if(archivedBuilds.isEmpty()){
            lLArchiveEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        val adapter = ArchiveListRecyclerViewAdapter(
            this, archivedBuilds,
            archiveViewModel,
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    lLArchiveEmptyList.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
                }
            }
        )
        recyclerView.adapter = adapter
//        recyclerView.layoutManager = ArchivedListLayoutManager(this,1)
        recyclerView.layoutManager = LinearLayoutManager(this)

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

    override fun onResume() {
        super.onResume()
        val emptyListMessage = findViewById<LinearLayout>(R.id.layout_emptyList)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_builds)

        app = application as BuzyUserAppSession
        pcBuildList = app.buildList

        val archivedBuilds = pcBuildList.filter { !it.isDeleted && it.isArchived} as ArrayList<PCBuild>
        if(archivedBuilds.isEmpty()){
            emptyListMessage?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyListMessage?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }

//        recyclerView?.layoutAnimation = null
//        recyclerView?.doOnLayout {
//            (recyclerView.layoutManager as AnimatedGridLayoutManager).animateItemsIn()
//        }
    }

}