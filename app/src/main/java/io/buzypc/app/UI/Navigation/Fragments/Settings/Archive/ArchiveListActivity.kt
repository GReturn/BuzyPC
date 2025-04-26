package io.buzypc.app.UI.Navigation.Fragments.Settings.Archive

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.OnBuildListChangedListener
import io.buzypc.app.UI.Navigation.ViewModels.ListsInformationViewModel
import io.buzypc.app.UI.Utils.LayoutManagers.ArchivedListLayoutManager

class ArchiveListActivity : AppCompatActivity() {
    private lateinit var app: BuzyUserAppSession
    private lateinit var archivedBuildsList: ArrayList<PCBuild>
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
        archivedBuildsList = app.buildList

        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_archivedBuilds)
        val tvArchiveEmptyList = findViewById<TextView>(R.id.tvEmptyArchiveList)

        val archivedBuilds = archivedBuildsList.filter{!it.isDeleted && it.isArchived} as ArrayList<PCBuild>
        if(archivedBuilds.isEmpty()){
            tvArchiveEmptyList.visibility = View.VISIBLE
            recyclerView.visibility = View.INVISIBLE
        }

        val adapter = ArchiveListRecyclerViewAdapter(
            this, archivedBuilds,
            archiveViewModel,
            listsInformationViewModel,
            object : OnBuildListChangedListener {
                override fun onBuildListChanged(isEmpty: Boolean) {
                    tvArchiveEmptyList.visibility = if (isEmpty) View.VISIBLE else View.GONE
                }
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = ArchivedListLayoutManager(this,1)

    }
}