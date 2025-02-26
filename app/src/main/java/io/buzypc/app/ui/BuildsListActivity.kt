package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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

        val btnProfileView = findViewById<ImageView>(R.id.button_profile_view)
        btnProfileView.setOnClickListener(){
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_builds)
        setPCModelList()

        val adapter = PCBuildAdapter(this, pcModelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)


    }
    private fun setPCModelList() {
        val pcModelNames = getResources().getStringArray(R.array.pc_builds)
        for (i in pcModelNames.indices) {
            pcModelList.add(PCModel(pcModelNames[i]))
        }
    }



}