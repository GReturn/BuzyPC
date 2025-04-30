package io.buzypc.app.UI.Navigation.Fragments.BuildTracker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.progressindicator.LinearProgressIndicator
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.formatDecimalPriceToPesoCurrencyString
import io.buzypc.app.UI.Utils.saveBuildList
import io.buzypc.app.UI.Widget.DialogView.CustomInfoDialogView

class TrackedBuildChecklistActivity : AppCompatActivity() {
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var progressText: TextView
    private lateinit var progressPercentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracked_build_checklist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val app = application as BuzyUserAppSession
        val focusedBuild = app.selectedBuildToViewChecklist

        val btnBack: ImageView = findViewById(R.id.btn_back_navigation)
        val btnInfo: ImageView = findViewById(R.id.btn_see_information)
        val tvBuildName: TextView = findViewById(R.id.tvBuildName)
        val tvBuildBudget: TextView = findViewById(R.id.tvBuildBudget)

        btnBack.setOnClickListener {
            finish()
        }

        btnInfo.setOnClickListener {
            CustomInfoDialogView(this)
                .setTitle("What's this?")
                .setDescription(getString(R.string.tips_description))
                .setOnConfirmClickListener {  }
                .show()
        }

        tvBuildName.text = focusedBuild.name
        val formattedTotal = formatDecimalPriceToPesoCurrencyString(focusedBuild.getTotalPrice())
        tvBuildBudget.text = getString(R.string.checklist_grand_total, formattedTotal)

        val initialProgress = focusedBuild.getProgress().first
        val totalComponents = focusedBuild.getProgress().second
        progressText = findViewById(R.id.tv_progressCompleted)
        progressText.text = getString(
            R.string.progress_completed_of_total_components_bought,
            initialProgress,
            totalComponents
        )
        progressPercentage = findViewById(R.id.tv_percentageCompleted)
        progressPercentage.text = "${(initialProgress.toDouble()/totalComponents*100).toInt()}%"

        progressIndicator = findViewById(R.id.linearProgressIndicator)
        updateProgressUI(initialProgress, totalComponents)


        // Motherboard
        val checkboxMotherboardBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentMotherboardBoughtStatusMark)
        val tvMotherboardName: TextView = findViewById(R.id.textView_componentMotherboardName)
        val tvMotherboardPrice: TextView = findViewById(R.id.textView_componentMotherboardPrice)
        tvMotherboardName.text = focusedBuild.pc.motherboard.name

        tvMotherboardPrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.motherboard.price)
        )

        checkboxMotherboardBoughtStatus.isChecked = focusedBuild.pc.motherboard.isBought
        checkboxMotherboardBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.motherboard.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }


        // CPU
        val checkboxCPUBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentCPUBoughtStatusMark)
        val tvCPUName: TextView = findViewById(R.id.textView_componentCPUName)
        val tvCPUPrice: TextView = findViewById(R.id.textView_componentCPUPrice)
        tvCPUName.text = focusedBuild.pc.cpu.name

        tvCPUPrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.cpu.price)
        )

        checkboxCPUBoughtStatus.isChecked = focusedBuild.pc.cpu.isBought
        checkboxCPUBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.cpu.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }


        // GPU
        val checkboxGPUBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentGPUBoughtStatusMark)
        val tvGPUName: TextView = findViewById(R.id.textView_componentGPUName)
        val tvGPUPrice: TextView = findViewById(R.id.textView_componentGPUPrice)
        tvGPUName.text = focusedBuild.pc.gpu.name

        tvGPUPrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.gpu.price)
        )

        checkboxGPUBoughtStatus.isChecked = focusedBuild.pc.gpu.isBought
        checkboxGPUBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.gpu.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }


        // RAM
        val checkboxRAMBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentRAMBoughtStatusMark)
        val tvRAMName: TextView = findViewById(R.id.textView_componentRAMName)
        val tvRAMPrice: TextView = findViewById(R.id.textView_componentRAMPrice)
        tvRAMName.text = focusedBuild.pc.ram.name

        tvRAMPrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.ram.price)
        )

        checkboxRAMBoughtStatus.isChecked = focusedBuild.pc.ram.isBought
        checkboxRAMBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.ram.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }


        // Storage
        val checkboxStorageBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentStorageBoughtStatusMark)
        val tvStorageName: TextView = findViewById(R.id.textView_componentStorageName)
        val tvStoragePrice: TextView = findViewById(R.id.textView_componentStoragePrice)
        tvStorageName.text = focusedBuild.pc.storageDevice.name

        tvStoragePrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.storageDevice.price)
        )

        checkboxStorageBoughtStatus.isChecked = focusedBuild.pc.storageDevice.isBought
        checkboxStorageBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.storageDevice.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }


        // PSU
        val checkboxPSUBoughtStatus: MaterialCheckBox = findViewById(R.id.checkbox_componentPSUBoughtStatusMark)
        val tvPSUName: TextView = findViewById(R.id.textView_componentPSUName)
        val tvPSUPrice: TextView = findViewById(R.id.textView_componentPSUPrice)
        tvPSUName.text = focusedBuild.pc.psu.name

        tvPSUPrice.text = getString(
            R.string.phpAmount_1_s,
            formatDecimalPriceToPesoCurrencyString(focusedBuild.pc.psu.price)
        )

        checkboxPSUBoughtStatus.isChecked = focusedBuild.pc.psu.isBought
        checkboxPSUBoughtStatus.setOnCheckedChangeListener { _, isChecked ->
            focusedBuild.pc.psu.isBought = isChecked

            saveBuildList(this, app.buildList)
            val initProgress = focusedBuild.getProgress().first
            val maxProgress = focusedBuild.getProgress().second
            updateProgressUI(initProgress, maxProgress)
        }
    }

    private fun updateProgressUI(completedItems: Int, totalItems: Int) {
        val progress = if (totalItems == 0) 0 else (completedItems * 100 / totalItems)

        // Set the progress (post to ensure UI thread safety)
        progressIndicator.post {
            progressIndicator.setProgressCompat(progress, true)
        }
        progressText.text = getString(
            R.string.progress_completed_of_total_components_bought,
            completedItems,
            totalItems
        )
        progressPercentage.text = "$progress%"
    }

}