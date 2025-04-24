package io.buzypc.app.UI.Navigation.Fragments.NewBuild

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.UI.Navigation.Fragments.BuildList.BuyComponentActivity
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Widget.RadarChartViewFragment

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
        val tvBuildTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)
        val btnSaveButton = findViewById<Button>(R.id.btn_SaveBuild)
        val app = application as BuzyUserAppSession


        tvBuildName.text = "${(application as BuzyUserAppSession).buildName}'s Summary"

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()

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

        val btnSeeCPUStores = findViewById<Button>(R.id.btnSeeStoresCPU)
        val btnSeeGPUsStores = findViewById<Button>(R.id.btnSeeStoresGPU)
        val btnSeeMotherboardStores = findViewById<Button>(R.id.btnSeeStoresMotherboard)
        val btnSeePSUStores = findViewById<Button>(R.id.btnSeeStoresPSU)
        val btnSeeRAMStores = findViewById<Button>(R.id.btnSeeStoresRAM)
        val btnSeeStorageStores = findViewById<Button>(R.id.btnSeeStoresStorage)


        btnSeeCPUStores.setOnClickListener {
            app.component = app.pc.cpu
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }

        btnSeeGPUsStores.setOnClickListener {
            app.component = app.pc.gpu
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }

        btnSeeMotherboardStores.setOnClickListener {
            app.component = app.pc.motherboard
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }

        btnSeePSUStores.setOnClickListener {
            app.component = app.pc.psu
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }

        btnSeeRAMStores.setOnClickListener {
            app.component = app.pc.ram
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }

        btnSeeStorageStores.setOnClickListener {
            app.component = app.pc.storageDevice
            startActivity(Intent(this, BuyComponentActivity::class.java))
        }


        setCompatScore(app, compatCPU, compatGPU, compatPSU, compatRam, compatStorage)
        setComponentPrices(app, priceCPU,priceGPU, priceMotherboard, pricePSU, priceRAM, priceStorage)
        setComponentNames(app, nameCPU, nameGPU, nameMotherboard, namePSU, nameRAM, nameStorage)
        setTotalPrice(app, tvBuildTotalPrice)

        fragmentTransaction.replace(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately

        btnSaveButton.setOnClickListener {
            val buildManager = BuzyUserBuildPrefManager(this)
            val newBuild = PCBuild(
                generateUniqueBuildId(this),
                app.buildName,
                app.pc.getTotalPrice(),
                app.pc
            )

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
    }
    fun setTotalPrice(app: BuzyUserAppSession, tvTotalPrice: TextView) {
        tvTotalPrice.text = app.pc.getTotalPrice().toString()
    }

    fun setComponentPrices(app: BuzyUserAppSession, priceCPU: TextView, priceGPU: TextView, priceMotherboard: TextView, pricePSU: TextView, priceRAM: TextView, priceStorage: TextView) {
        priceCPU.text = "PHP %.2f".format(app.pc.cpu.price)
        priceGPU.text = "PHP %.2f".format(app.pc.gpu.price)
        priceMotherboard.text = "PHP %.2f".format(app.pc.motherboard.price)
        priceRAM.text = "PHP %.2f".format(app.pc.ram.price)
        pricePSU.text = "PHP %.2f".format(app.pc.psu.price)
        priceStorage.text = "PHP %.2f".format(app.pc.storageDevice.price)
    }


    fun setCompatScore(app: BuzyUserAppSession, compatCPU: TextView, compatGPU: TextView, compatPSU: TextView, compatRam: TextView, compatStorage: TextView) {
        compatCPU.text = app.pc.cpu.performanceScore.toString()
        compatGPU.text = app.pc.gpu.performanceScore.toString()
        compatPSU.text = app.pc.psu.performanceScore.toString()
        compatRam.text = app.pc.ram.performanceScore.toString()
        compatStorage.text = app.pc.storageDevice.performanceScore.toString()
    }

    fun setComponentNames(app: BuzyUserAppSession, nameCPU: TextView, nameGPU: TextView, nameMotherboard: TextView, namePSU: TextView, nameRAM: TextView, nameStorage: TextView) {
        nameCPU.text = app.pc.cpu.name
        nameGPU.text = app.pc.gpu.name
        nameMotherboard.text = app.pc.motherboard.name
        namePSU.text = app.pc.psu.name
        nameStorage.text = app.pc.storageDevice.name
        nameRAM.text = app.pc.ram.name
    }
}