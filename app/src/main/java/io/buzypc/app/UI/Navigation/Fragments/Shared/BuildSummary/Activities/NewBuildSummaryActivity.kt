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
import androidx.recyclerview.widget.LinearSnapHelper
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
import io.buzypc.app.UI.Utils.formatDecimalPriceToPesoCurrencyString
import io.buzypc.app.UI.Widget.DialogView.CustomInfoDialogView
import io.buzypc.app.UI.Widget.RadarChartViewFragment
import java.util.Timer
import java.util.TimerTask

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
            app.buildBudget,
            app.currentPCToBuild
        )
        app.selectedBuildToSummarize = newBuild
        val pcBuild = app.selectedBuildToSummarize
        val savingsAmount = pcBuild.budget - pcBuild.getTotalPrice()

        if (savingsAmount < 0) {
            CustomInfoDialogView(this)
                .setTitle("Budget Exceeded")
                .setDescription("The total cost of this build exceeds your budget by ${formatDecimalPriceToPesoCurrencyString(-savingsAmount)}. You may choose to keep this build or choose another.")
                .setOnConfirmClickListener { }
                .show()
        }

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
        val tvBudget = findViewById<TextView>(R.id.tv_BuildBudget)
        val tvTotalCost = findViewById<TextView>(R.id.tv_TotalCost)
        val tvSavings = findViewById<TextView>(R.id.tv_Savings)

        val initialBudget = formatDecimalPriceToPesoCurrencyString(pcBuild.budget)
        val totalCost = formatDecimalPriceToPesoCurrencyString(pcBuild.getTotalPrice())
        val savings = formatDecimalPriceToPesoCurrencyString(savingsAmount)

        tvBuildName.text = pcBuild.name
        tvBudget.text = getString(R.string.phpAmount_1_s, initialBudget)
        tvTotalCost.text = getString(R.string.phpAmount_1_s, totalCost)
        tvSavings.text = getString(R.string.phpAmount_1_s, savings)

        // Component List
        val recyclerViewComponents = findViewById<RecyclerView>(R.id.recycleComponents)

        val components = ArrayList<Component>()
        components.add(pcBuild.pc.motherboard)
        components.add(pcBuild.pc.cpu)
        components.add(pcBuild.pc.gpu)
        components.add(pcBuild.pc.ram)
        components.add(pcBuild.pc.storageDevice)
        components.add(pcBuild.pc.psu)

        val adapter = BuildComponentRecyclerViewAdapter(this, components)

        recyclerViewComponents.adapter = adapter
        recyclerViewComponents.setHasFixedSize(true)
        recyclerViewComponents.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recyclerViewComponents.addItemDecoration(HorizontalSpaceItemDecoration(12))
        setAutoScroll(recyclerViewComponents, recyclerViewComponents.layoutManager as LinearLayoutManager, adapter, 3000)

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()
        fragmentTransaction.add(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        val tvPerformanceScore = findViewById<TextView>(R.id.tvPerformanceRatio)
        val rating = pcBuild.pc.getPerformanceRatingTier().description
        tvPerformanceScore.text = getString(R.string.performance_to_budget_ratio_1_s, rating)

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately
    }

    // Provided by ParSa in StackOverflow: https://stackoverflow.com/a/56872365/14139842
    private fun setAutoScroll(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: BuildComponentRecyclerViewAdapter,
        interval: Long
    ) {
        //The LinearSnapHelper will snap the center of the target child view to the center of the
        // attached RecyclerView , it's optional
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(recyclerView)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                recyclerView.post {
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val itemCount = adapter.itemCount

                    if (lastVisiblePosition == RecyclerView.NO_POSITION || itemCount == 0) return@post

                    val nextPosition = if (lastVisiblePosition < itemCount - 1) {
                        lastVisiblePosition + 1
                    } else {
                        0
                    }

                    recyclerView.smoothScrollToPosition(nextPosition)
                }
            }
        }, 0, interval)
    }
}