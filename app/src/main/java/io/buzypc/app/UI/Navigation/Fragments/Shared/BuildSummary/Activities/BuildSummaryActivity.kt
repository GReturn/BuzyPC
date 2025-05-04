package io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.BuildComponentRecyclerViewAdapter
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.HorizontalSpaceItemDecoration
import io.buzypc.app.UI.Utils.formatDecimalPriceToPesoCurrencyString
import io.buzypc.app.UI.Utils.loadCurrentUserDetails
import io.buzypc.app.UI.Widget.RadarChartViewFragment
import java.util.Timer
import java.util.TimerTask

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
        val pcBuild = app.selectedBuildToSummarize

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
        val tvSavingsTitle = findViewById<TextView>(R.id.tv_Savings_title)
        val tvSavings = findViewById<TextView>(R.id.tv_Savings)
        val tvElipses = findViewById<TextView>(R.id.tv_elipses)

        val savingsAmount = pcBuild.budget - pcBuild.getTotalPrice()

        val initialBudget = formatDecimalPriceToPesoCurrencyString(pcBuild.budget)
        val totalCost = formatDecimalPriceToPesoCurrencyString(pcBuild.getTotalPrice())
        val savings = formatDecimalPriceToPesoCurrencyString(savingsAmount)

        tvBuildName.text = pcBuild.name
        tvBudget.text = getString(R.string.phpAmount_1_s, initialBudget)
        tvTotalCost.text = getString(R.string.phpAmount_1_s, totalCost)
        tvSavings.text = getString(R.string.phpAmount_1_s, savings)

        if(savingsAmount < 0) {
            val redColor = ContextCompat.getColor(this, R.color.bz_destructive_red)

            tvSavingsTitle.setTextColor(redColor)
            tvSavings.setTextColor(redColor)

            tvSavingsTitle.compoundDrawableTintList = ContextCompat
                .getColorStateList(this, R.color.bz_destructive_red)
        }

        tvElipses.setOnClickListener(){
            val pm = PopupMenu(this@BuildSummaryActivity, tvElipses)
            pm.menuInflater.inflate(R.menu.build_summary_menu, pm.menu)
            pm.setOnMenuItemClickListener {
                val buildManager = BuzyUserBuildPrefManager(this)
                val app = application as BuzyUserAppSession
                val pcBuild = app.selectedBuildToSummarize
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_build_summary_rename_build, null)
                val newBuildName = dialogView.findViewById<TextInputEditText>(R.id.edittext_rename_build)

                val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
                val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

                val dialog = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create()

                btnConfirm.setOnClickListener {
                    val name = newBuildName.text.toString().trim()
                    if (name.isBlank()) {
                        newBuildName.error = "Name can’t be empty"
                        return@setOnClickListener
                    }
                    pcBuild.name = newBuildName.text.toString()
                    buildManager.renameBuild(app.username, pcBuild)
                    app.selectedBuildToSummarize.name = name
                    tvBuildName.text = name
                    Toast.makeText(this, "Build renamed successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
                true
            }
            pm.show()
        }

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
        recyclerViewComponents.setHasFixedSize(true)

        recyclerViewComponents.addItemDecoration(HorizontalSpaceItemDecoration(12))
        setAutoScroll(recyclerViewComponents, recyclerViewComponents.layoutManager as LinearLayoutManager, adapter, 3000)

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()
        fragmentTransaction.add(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()
        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately

        val tvPerformanceScore = findViewById<TextView>(R.id.tvPerformanceRatio)
        val rating = pcBuild.pc.getPerformanceRatingTier().description
        tvPerformanceScore.text = getString(R.string.performance_to_budget_ratio_1_s, rating)
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

    private fun showRenameBuildDialog() {

    }

}