package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.appsession.BuzyUserAppSession
import io.buzypc.app.ui.navigation.BottomNavigationActivity
import io.buzypc.app.ui.utils.loadCurrentUserDetails
import io.buzypc.app.ui.widget.RadarChartViewFragment

class NewBuildSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_build_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tvBuildName = findViewById<TextView>(R.id.tv_BuildSummary)
        val btnSaveButton = findViewById<Button>(R.id.btn_SaveBuild)
        val user = loadCurrentUserDetails(this)
        val app = application as BuzyUserAppSession


        user.retrieveBuilds()
        tvBuildName.text = "${(application as BuzyUserAppSession).buildName}'s Summary"

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()

        val priceCPU = findViewById<TextView>(R.id.tvPriceCPU)
        val priceGPU = findViewById<TextView>(R.id.tvPriceGPU)
        val priceMotherboard = findViewById<TextView>(R.id.tvPriceMotherboard)
        val pricePSU = findViewById<TextView>(R.id.tvPricePSU)
        val priceRAM = findViewById<TextView>(R.id.tvPriceRAM)
        val priceStorage = findViewById<TextView>(R.id.tvPriceStorage)

        val nameCPU = findViewById<TextView>(R.id.tvCPU)
        val nameGPU = findViewById<TextView>(R.id.tvGPU)
        val nameMotherboard = findViewById<TextView>(R.id.tvMotherboard)
        val nameRAM = findViewById<TextView>(R.id.tvRAM)
        val namePSU = findViewById<TextView>(R.id.tvPSU)
        val nameStorage = findViewById<TextView>(R.id.tvStorage)

        val compatCPU = findViewById<TextView>(R.id. tvHeaderCpuCompat)
        val compatGPU= findViewById<TextView>(R.id.tvHeaderGPUCompat)
        val compatPSU = findViewById<TextView>(R.id.tvHeaderPSUCompat)
        val compatRam = findViewById<TextView>(R.id.tvHeaderRAMCompat)
        val compatStorage = findViewById<TextView>(R.id.tvHeaderStorageCompat)

        val cbCPU = findViewById<CheckBox>(R.id.cbCpu)
        val cbGPU = findViewById<CheckBox>(R.id.cbGpu)
        val cbMotherboard = findViewById<CheckBox>(R.id.cbMotherboard)
        val cbPSU = findViewById<CheckBox>(R.id.cbPSU)
        val cbRAM = findViewById<CheckBox>(R.id.cbRAM)
        val cbStorage = findViewById<CheckBox>(R.id.cbStorage)

        setCompatScore(app, compatCPU, compatGPU, compatPSU, compatRam, compatStorage)
        hideAndDisableAllCheckboxes(cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage)
        setComponentPrices(app, priceCPU,priceGPU, priceMotherboard, pricePSU, priceRAM, priceStorage)
        setComponentNames(app, nameCPU, nameGPU, nameMotherboard, namePSU, nameRAM, nameStorage)


        fragmentTransaction.replace(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately

        btnSaveButton.setOnClickListener {
            // Add current build details to global lists (if needed)
            user.buildNameList.add((application as BuzyUserAppSession).buildName)
            user.buildBudgetList.add((application as BuzyUserAppSession).buildBudget)

            // Save build data into the user-specific SharedPreferences via BuzyUser
            user.saveBuilds()
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
    }
    fun setComponentPrices(app: BuzyUserAppSession, priceCPU: TextView, priceGPU: TextView, priceMotherboard: TextView, pricePSU: TextView, priceRAM: TextView, priceStorage: TextView,){
        priceCPU.text = "PHP ${app.myPC.cpuPrice}"
        priceGPU.text = "PHP ${app.myPC.gpuPrice}"
        priceMotherboard.text = "PHP ${app.myPC.motherboardPrice}"
        priceRAM.text = "PHP ${app.myPC.ramPrice}"
        pricePSU.text = "PHP ${app.myPC.psuPrice}"
        priceStorage.text = "PHP ${app.myPC.storageDevicePrice}"
    }

    fun setComponentNames(app: BuzyUserAppSession, nameCPU: TextView, nameGPU: TextView, nameMotherboard: TextView, namePSU: TextView, nameRAM: TextView, nameStorage: TextView,){
        nameCPU.text = app.myPC.cpuName
        nameGPU.text = app.myPC.gpuName
        nameMotherboard.text = app.myPC.motherboardName
        namePSU.text = app.myPC.psuName
        nameStorage.text = app.myPC.storageDeviceName
        nameRAM.text = app.myPC.ramName

    }

    private fun hideAndDisableAllCheckboxes(
        cbCPU: CheckBox,
        cbGPU: CheckBox,
        cbMotherboard: CheckBox,
        cbPSU: CheckBox,
        cbRAM: CheckBox,
        cbStorage: CheckBox
    ) {
        listOf(cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage).forEach { cb ->
            cb.visibility = View.INVISIBLE
            cb.isEnabled  = false
        }
    }

    fun setCompatScore(app: BuzyUserAppSession, compatCPU: TextView, compatGPU: TextView, compatPSU: TextView, compatRam: TextView, compatStorage: TextView){
        compatCPU.text = app.myPC.cpuCompatibility.toString()
        compatGPU.text = app.myPC.gpuCompatibility.toString()
        compatPSU.text = app.myPC.psuCompatibility.toString()
        compatRam.text = app.myPC.ramCompatibility.toString()
        compatStorage.text = app.myPC.storageDeviceCompatibility.toString()
    }


}