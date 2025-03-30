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
import io.buzypc.app.data.appsession.BuzyUserAppSession
import io.buzypc.app.ui.navigation.BottomNavigation
import io.buzypc.app.ui.utils.loadCurrentUserDetails

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
        val user = loadCurrentUserDetails(this)
        user.retrieveBuilds()
        tvBuildName.text = "${(application as BuzyUserAppSession).buildName}'s Summary"

        btnSaveBuild.setOnClickListener {
            // Add current build details to global lists (if needed)
            user.buildNameList.add((application as BuzyUserAppSession).buildName)
            user.buildBudgetList.add((application as BuzyUserAppSession).buildBudget)

            // Save build data into the user-specific SharedPreferences via BuzyUser
            user.saveBuilds()
            val intent = Intent(this, BottomNavigation::class.java)

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
}