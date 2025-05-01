package io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.BuildComponentRecyclerViewAdapter
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.HorizontalSpaceItemDecoration
import io.buzypc.app.UI.Utils.formatDecimalPriceToPesoCurrencyString
import io.buzypc.app.UI.Widget.RadarChartViewFragment

class BuildSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_build_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_coordinator)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val app = application as BuzyUserAppSession

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

        val initialBudget = formatDecimalPriceToPesoCurrencyString(app.selectedBuildToSummarize.budget)
        val totalCost = formatDecimalPriceToPesoCurrencyString(app.selectedBuildToSummarize.getTotalPrice())
        val savings = formatDecimalPriceToPesoCurrencyString(
            app.selectedBuildToSummarize.getTotalPrice() - app.selectedBuildToSummarize.budget
        )

        tvBuildName.text = app.selectedBuildToSummarize.name
        tvBudget.text = getString(R.string.phpAmount_1_s, initialBudget)
        tvTotalCost.text = getString(R.string.phpAmount_1_s, totalCost)
        tvSavings.text = getString(R.string.phpAmount_1_s, savings)

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
        recyclerViewComponents.setHasFixedSize(true)

        recyclerViewComponents.addItemDecoration(HorizontalSpaceItemDecoration(12))
        setAutoScroll(recyclerViewComponents, recyclerViewComponents.layoutManager as LinearLayoutManager, adapter, 3000)

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

        val tvPerformanceScore = findViewById<TextView>(R.id.tvPerformanceRatio)
        val rating = app.selectedBuildToSummarize.pc.getPerformanceRatingTier().description
        tvPerformanceScore.text = getString(R.string.performance_to_budget_ratio_1_s, rating)

        setCompatScore(app, compatCPU, compatGPU, compatPSU, compatRam, compatStorage)
    }

    fun setCompatScore(app: BuzyUserAppSession, compatCPU: TextView, compatGPU: TextView, compatPSU: TextView, compatRam: TextView, compatStorage: TextView) {
        compatCPU.text = app.pc.cpu.performanceScore.toString()
        compatGPU.text = app.pc.gpu.performanceScore.toString()
        compatPSU.text = app.pc.psu.performanceScore.toString()
        compatRam.text = app.pc.ram.performanceScore.toString()
        compatStorage.text = app.pc.storageDevice.performanceScore.toString()
    }

    // Provided by ParSa in StackOverflow: https://stackoverflow.com/a/56872365/14139842
    // We replace the Timer with a Handler for better control on the main/UI thread.
    private fun setAutoScroll(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: BuildComponentRecyclerViewAdapter,
        interval: Long = 3000L,
        pauseDuration: Long = 3000L // how long to pause after user interaction
    ) {
        //The LinearSnapHelper will snap the center of the target child view to the center of the
        // attached RecyclerView , it's optional
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(recyclerView)

        val handler = Handler(Looper.getMainLooper())
        var autoScrollEnabled = true
        var autoScrollRunnable: Runnable? = null

        fun startAutoScroll() {
            autoScrollRunnable?.let { handler.removeCallbacks(it) } // clear existing
            autoScrollRunnable = object : Runnable {
                override fun run() {
                    if (autoScrollEnabled) {
                        val nextPos = if (layoutManager.findLastCompletelyVisibleItemPosition() < adapter.itemCount - 1) {
                            layoutManager.findLastCompletelyVisibleItemPosition() + 1
                        } else {
                            0
                        }
                        recyclerView.smoothScrollToPosition(nextPos)
                    }
                    handler.postDelayed(this, interval)
                }
            }
            handler.postDelayed(autoScrollRunnable!!, interval)
        }

        // Pause on user interaction
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    autoScrollEnabled = false
                    handler.removeCallbacks(autoScrollRunnable!!)
                } else {
                    autoScrollEnabled = true
                    handler.postDelayed(autoScrollRunnable!!, pauseDuration)
                }
            }
        })

        startAutoScroll()
    }

}