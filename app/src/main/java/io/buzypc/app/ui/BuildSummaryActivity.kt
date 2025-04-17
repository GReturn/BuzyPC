package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class BuildSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_build_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val user = loadCurrentUserDetails(this)
        val tvBuildName = findViewById<TextView>(R.id.tv_BuildSummary)
        val app = application as BuzyUserAppSession

        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)
        tvTotalPrice.paintFlags = tvTotalPrice.paintFlags


        val priceCPU = findViewById<TextView>(R.id.tvPriceCPU)
        val priceGPU = findViewById<TextView>(R.id.tvPriceGPU)
        val priceMotherboard = findViewById<TextView>(R.id.tvPriceMotherboard)
        val pricePSU = findViewById<TextView>(R.id.tvPricePSU)
        val priceRAM = findViewById<TextView>(R.id.tvPriceRAM)
        val priceStorage = findViewById<TextView>(R.id.tvPriceStorage)

        val nameCPU = findViewById<TextView>(R.id.tvCPUName)
        val nameGPU = findViewById<TextView>(R.id.tvGPUName)
        val nameMotherboard = findViewById<TextView>(R.id.tvMotherboardName)
        val nameRAM = findViewById<TextView>(R.id.tvRAMName)
        val namePSU = findViewById<TextView>(R.id.tvPSUName)
        val nameStorage = findViewById<TextView>(R.id.tvStorageName)

        val compatCPU = findViewById<TextView>(R.id. tvCPUSCore)
        val compatGPU= findViewById<TextView>(R.id.tvGPUSCore)
        val compatPSU = findViewById<TextView>(R.id.tvPSUScore)
        val compatRam = findViewById<TextView>(R.id.tvRAMScore)
        val compatStorage = findViewById<TextView>(R.id.tvStorageScore)


        user.retrieveBuilds()
        tvBuildName.text = "${(application as BuzyUserAppSession).buildName}'s Summary"

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()
        fragmentTransaction.add(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately
        setCompatScore(app, compatCPU, compatGPU, compatPSU, compatRam, compatStorage)
        setTotalPrice(app, tvTotalPrice)
        setComponentPrices(app, priceCPU,priceGPU, priceMotherboard, pricePSU, priceRAM, priceStorage)
        setComponentNames(app, nameCPU, nameGPU, nameMotherboard, namePSU, nameRAM, nameStorage)


//        cbCPU.isChecked         = user.getComponentStatus(app.buildName, "cpu")
//        cbGPU.isChecked         = user.getComponentStatus(app.buildName, "gpu")
//        cbMotherboard.isChecked = user.getComponentStatus(app.buildName, "motherboard")
//        cbPSU.isChecked         = user.getComponentStatus(app.buildName, "psu")
//        cbRAM.isChecked         = user.getComponentStatus(app.buildName, "ram")
//        cbStorage.isChecked     = user.getComponentStatus(app.buildName, "storage")
//        updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)

//
//        cbCPU.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"cpu", isChecked)
//            updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//        }
//
//        cbGPU.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"gpu", isChecked)
//            updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//        }
//
//        cbMotherboard.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"motherboard", isChecked)
//            updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//
//        }
//
//        cbPSU.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"psu", isChecked)
//            updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//        }
//
//        cbRAM.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"ram", isChecked)
//            updateTotalPrice(app,cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//        }
//
//        cbStorage.setOnCheckedChangeListener { _, isChecked ->
//            user.saveComponentStatus(app.buildName,"storage", isChecked)
//            updateTotalPrice(app, cbCPU, cbGPU, cbMotherboard, cbPSU, cbRAM, cbStorage, tvBuildTotalPrice)
//        }


    }
    fun updateTotalPrice(app : BuzyUserAppSession,cbCPU: CheckBox, cbGPU: CheckBox, cbMotherboard: CheckBox, cbPSU: CheckBox, cbRAM: CheckBox, cbStorage: CheckBox, tvBuildTotalPrice: TextView) {
        var totalPrice = 0.00

        if (cbCPU.isChecked)         totalPrice += app.myPC.cpuPrice
        if (cbGPU.isChecked)         totalPrice += app.myPC.gpuPrice
        if (cbMotherboard.isChecked) totalPrice += app.myPC.motherboardPrice
        if (cbPSU.isChecked)         totalPrice += app.myPC.psuPrice
        if (cbRAM.isChecked)         totalPrice += app.myPC.ramPrice
        if (cbStorage.isChecked)     totalPrice += app.myPC.storageDevicePrice

        tvBuildTotalPrice.text = "TOTAL: PHP $totalPrice"
    }

    fun setTotalPrice(app: BuzyUserAppSession, tvTotalPrice: TextView){
        var total = 0.00
        total += app.myPC.gpuPrice
        total += app.myPC.motherboardPrice
        total += app.myPC.psuPrice
        total += app.myPC.ramPrice
        total += app.myPC.storageDevicePrice
        tvTotalPrice.text = "Total: PHP $total"
    }

    fun setComponentPrices(app: BuzyUserAppSession, priceCPU: TextView, priceGPU: TextView, priceMotherboard: TextView, pricePSU: TextView, priceRAM: TextView, priceStorage: TextView,){
        priceCPU.text = "PHP %.2f".format(app.myPC.cpuPrice)
        priceGPU.text = "PHP %.2f".format(app.myPC.gpuPrice)
        priceMotherboard.text = "PHP %.2f".format(app.myPC.motherboardPrice)
        priceRAM.text = "PHP %.2f".format(app.myPC.ramPrice)
        pricePSU.text = "PHP %.2f".format(app.myPC.psuPrice)
        priceStorage.text = "PHP %.2f".format(app.myPC.storageDevicePrice)
    }

    fun setCompatScore(app: BuzyUserAppSession, compatCPU: TextView, compatGPU: TextView, compatPSU: TextView, compatRam: TextView, compatStorage: TextView){
        compatCPU.text = app.myPC.cpuCompatibility.toString()
        compatGPU.text = app.myPC.gpuCompatibility.toString()
        compatPSU.text = app.myPC.psuCompatibility.toString()
        compatRam.text = app.myPC.ramCompatibility.toString()
        compatStorage.text = app.myPC.storageDeviceCompatibility.toString()
    }

    fun setComponentNames(app: BuzyUserAppSession, nameCPU: TextView, nameGPU: TextView, nameMotherboard: TextView, namePSU: TextView, nameRAM: TextView, nameStorage: TextView,){
        nameCPU.text = app.myPC.cpuName
        nameGPU.text = app.myPC.gpuName
        nameMotherboard.text = app.myPC.motherboardName
        namePSU.text = app.myPC.psuName
        nameStorage.text = app.myPC.storageDeviceName
        nameRAM.text = app.myPC.ramName

    }

}