package io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Navigation.Fragments.NewBuild.generateUniqueBuildId
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.BuildComponentRecyclerViewAdapter
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.HorizontalSpaceItemDecoration
import io.buzypc.app.UI.Utils.formatDecimalPriceToString
import io.buzypc.app.UI.Widget.RadarChartViewFragment

class NewBuildSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_build_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_summary_body)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val app = application as BuzyUserAppSession

        // TODO: should get newBuild from our AI
        val newBuild = PCBuild(
            generateUniqueBuildId(this),
            app.buildName,
            app.pc.getTotalPrice(),
            app.pc
        )
        app.selectedBuildToSummarize = newBuild

        val btnSaveButton = findViewById<Button>(R.id.btn_SaveBuild)
        btnSaveButton.setOnClickListener {
            val buildManager = BuzyUserBuildPrefManager(this)
            buildManager.addBuild(app.username, newBuild)
            app.loadBuildList()

            val intent = Intent(this, BottomNavigationActivity::class.java)

            /*
                we set these flags to fix the issue of erratic screen flickering produced by
                doing the following procedures:
                 1. change the theme to dark mode to light mode or vice versa
                 2. add a build
                 3. save the build
                 4. change the theme again
            */
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }

        // Back
        val btnBack = findViewById<ImageView>(R.id.btn_back_navigation)
        btnBack.setOnClickListener {
            finish()
            return@setOnClickListener
        }

        // Scrolling
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val fab = findViewById<FloatingActionButton>(R.id.fabScrollToTop)
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = scrollView.scrollY
            val pxLimit = 200
            if (scrollY > pxLimit) fab.show() else fab.hide()
        }
        fab.setOnClickListener { scrollView.smoothScrollTo(0, 0) }

        // Main Build Info
        val tvBuildName = findViewById<TextView>(R.id.tv_BuildName)
        val tvTotalPrice = findViewById<TextView>(R.id.tv_BuildBudget)
        tvTotalPrice.paintFlags = tvTotalPrice.paintFlags
        tvBuildName.text = "${(application as BuzyUserAppSession).selectedBuildToSummarize.name}"
        val initialBudget = formatDecimalPriceToString(app.pc.getTotalPrice())
        tvTotalPrice.text = getString(R.string.build_summary_initial_budget, initialBudget)

        // Component List
        val recyclerViewComponents = findViewById<RecyclerView>(R.id.recycleComponents)

        val components = ArrayList<Component>()
        components.add(app.pc.motherboard)
        components.add(app.pc.cpu)
        components.add(app.pc.gpu)
        components.add(app.pc.ram)
        components.add(app.pc.storageDevice)
        components.add(app.pc.psu)

        val adapter = BuildComponentRecyclerViewAdapter(this, components)

        recyclerViewComponents.adapter = adapter
        recyclerViewComponents.setHasFixedSize(true)
        recyclerViewComponents.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recyclerViewComponents.addItemDecoration(HorizontalSpaceItemDecoration(12))

        val compatCPU = findViewById<TextView>(R.id. tvCPUSCore)
        val compatGPU= findViewById<TextView>(R.id.tvGPUSCore)
        val compatPSU = findViewById<TextView>(R.id.tvPSUScore)
        val compatRam = findViewById<TextView>(R.id.tvRAMScore)
        val compatStorage = findViewById<TextView>(R.id.tvStorageScore)

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()
        fragmentTransaction.add(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately
        setCompatScore(app, compatCPU, compatGPU, compatPSU, compatRam, compatStorage)
    }

    fun setCompatScore(app: BuzyUserAppSession, compatCPU: TextView, compatGPU: TextView, compatPSU: TextView, compatRam: TextView, compatStorage: TextView) {
        compatCPU.text = app.pc.cpu.performanceScore.toString()
        compatGPU.text = app.pc.gpu.performanceScore.toString()
        compatPSU.text = app.pc.psu.performanceScore.toString()
        compatRam.text = app.pc.ram.performanceScore.toString()
        compatStorage.text = app.pc.storageDevice.performanceScore.toString()
    }
}