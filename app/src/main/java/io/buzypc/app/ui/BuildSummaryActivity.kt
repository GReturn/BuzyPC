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
        val tvBuildName = findViewById<TextView>(R.id.tv_BuildSummary)
        val user = loadCurrentUserDetails(this)
        val app = application as BuzyUserAppSession

        user.retrieveBuilds()
        tvBuildName.text = "${(application as BuzyUserAppSession).buildName}'s Summary"

        // Add the RadarChartView Fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val radarChartFragment = RadarChartViewFragment()
        fragmentTransaction.replace(R.id.radarChartContainer, radarChartFragment)
        fragmentTransaction.commit()

        supportFragmentManager.executePendingTransactions() // Ensure the fragment is added immediately

        val cbCPU = findViewById<CheckBox>(R.id.cbCpu)
        val cbGPU = findViewById<CheckBox>(R.id.cbGpu)
        val cbMotherboard = findViewById<CheckBox>(R.id.cbMotherboard)
        val cbPSU = findViewById<CheckBox>(R.id.cbPSU)
        val cbRAM = findViewById<CheckBox>(R.id.cbRAM)
        val cbStorage = findViewById<CheckBox>(R.id.cbStorage)

        cbCPU.isChecked         = user.getComponentStatus(app.buildName, "cpu")
        cbGPU.isChecked         = user.getComponentStatus(app.buildName, "gpu")
        cbMotherboard.isChecked = user.getComponentStatus(app.buildName, "motherboard")
        cbPSU.isChecked         = user.getComponentStatus(app.buildName, "psu")
        cbRAM.isChecked         = user.getComponentStatus(app.buildName, "ram")
        cbStorage.isChecked     = user.getComponentStatus(app.buildName, "storage")




        cbCPU.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"cpu", isChecked)
        }

        cbGPU.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"gpu", isChecked)
        }

        cbMotherboard.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"motherboard", isChecked)
        }

        cbPSU.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"psu", isChecked)
        }

        cbRAM.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"ram", isChecked)
        }

        cbStorage.setOnCheckedChangeListener { _, isChecked ->
            user.saveComponentStatus(app.buildName,"storage", isChecked)
        }

    }
}