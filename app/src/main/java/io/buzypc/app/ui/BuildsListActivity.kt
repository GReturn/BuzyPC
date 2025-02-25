package io.buzypc.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.buzypc.app.R
import io.buzypc.app.model.PCBuildAdapter
import io.buzypc.app.data.PCModel

class BuildsListActivity : AppCompatActivity() {
    val pcModelList = ArrayList<PCModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_builds_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_builds)
        setPCModelList()

        val adapter = PCBuildAdapter(this, pcModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)
    }
    private fun setPCModelList() {
        val pcModelnames = getResources().getStringArray(R.array.pc_builds)
        for (i in pcModelnames.indices) {
            pcModelList.add(PCModel(pcModelnames[i]))
        }
    }
}