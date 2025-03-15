package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.OurApplication
import io.buzypc.app.data.user.BuzyUser
import io.buzypc.app.ui.fragments.BuildListFragment
import io.buzypc.app.ui.fragments.NewBuildFragment

class BuildSummary : AppCompatActivity() {
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
        val btnSaveBuild = findViewById<Button>(R.id.btnSaveBuild)
        val user = BuzyUser(this, (application as OurApplication).username)
        user.retrieveBuilds()
        tvBuildName.text = "${(application as OurApplication).buildName}'s Summary"

        btnSaveBuild.setOnClickListener {
            // Add current build details to global lists (if needed)
            user.buildNameList.add((application as OurApplication).buildName)
            user.buildBudgetList.add((application as OurApplication).buildBudget)

            // Save build data into the user-specific SharedPreferences via BuzyUser
            user.saveBuilds()
            // Redirect to BottomNavigation (or another activity) as desired
            startActivity(Intent(this, BottomNavigation::class.java))
            finish()
        }
    }
}