package io.buzypc.app.UI.Widget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.buzypc.app.R
import io.buzypc.app.UI.Navigation.Fragments.Shared.BuildSummary.Activities.NewBuildSummaryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.buzypc.app.AI.BuzyAI
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.Utils.parsePCBuild
import java.io.File

class LoadingScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val appSession = applicationContext as BuzyUserAppSession
        val ai = BuzyAI()

        lifecycleScope.launch {
            // This will run on a background thread
            val buildName = appSession.buildName
            val budget = appSession.buildBudget

            // Generate the PC build (this might take some time)
            val xmlResult = withContext(Dispatchers.IO) {
                try {
                    ai.generatePCBuildXML(buildName, budget)
                } catch (e: Exception) {
                    Log.e("LoadingScreenActivity", "Error generating build", e)
                    ""
                }
            }
            saveXmlToFile(xmlResult, "$buildName.xml")

            val generatedPC = parsePCBuild(xmlResult)
            appSession.pc = generatedPC

            // Pass the result to the next activity
            val intent = Intent(this@LoadingScreenActivity, NewBuildSummaryActivity::class.java)
            Log.d("LoadingScreenActivity", "Done generating PC build. Starting NewBuildSummaryActivity.")

            startActivity(intent)
            finish()
        }
    }
    private suspend fun saveXmlToFile(xmlResult: String, fileName: String) {
        withContext(Dispatchers.IO) {
            // `context` is your Activity or Application context
            val fileName = "test.xml"
            val fileContents = xmlResult

            val file = File(this@LoadingScreenActivity.filesDir, fileName)
            file.writeText(fileContents)
        }
    }
}