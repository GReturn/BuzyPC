package io.buzypc.app.UI.Navigation.Fragments.Settings.Archive

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
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

        imgBtnBack.setOnClickListener(){
            finish()
        }


        pcBuildList = app.buildList

        val archivedBuilds = pcBuildList.filter{!it.isDeleted && it.isArchived} as ArrayList<PCBuild>
        if(archivedBuilds.isEmpty()){
            lLArchiveEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
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
        recyclerView.layoutManager = ArchivedListLayoutManager(this,1)
    }

    override fun onResume() {
        super.onResume()
        val emptyListMessage = findViewById<LinearLayout>(R.id.layout_emptyList)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_builds)

        app = application as BuzyUserAppSession
        pcBuildList = app.buildList

        val archivedBuilds = pcBuildList.filter { !it.isDeleted&& it.isArchived} as ArrayList<PCBuild>
        if(archivedBuilds.isEmpty()){
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

}