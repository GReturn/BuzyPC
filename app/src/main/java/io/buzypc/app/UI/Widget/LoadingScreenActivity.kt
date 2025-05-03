package io.buzypc.app.UI.Widget

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import io.buzypc.app.Data.BuildData.PC
import io.buzypc.app.Data.BuildData.Utils.ParseFailureException
import io.buzypc.app.Data.BuildData.Utils.parsePCBuild
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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

        // TODO set this to false if not yet ready for final version
        val shouldConnectToAI = appSession.useAI

        if(shouldConnectToAI) {
            val ai = BuzyAI()
            lifecycleScope.launch {

                if (!this@LoadingScreenActivity.isInternetAvailable()) {
                    Log.e("LoadingScreenActivity", "No internet connection available.")
                    Toast.makeText(this@LoadingScreenActivity, "No internet connection.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val buildName = appSession.buildName
                val budget = appSession.buildBudget

                var generatedPC: PC? = null
                var attempt = 0
                val maxTotalAttempts = 5 // Total attempts including both generation and parsing

                while (generatedPC == null && attempt < maxTotalAttempts) {
                    attempt++
                    try {
                        // Generate new XML
                        val xmlResult = withContext(Dispatchers.IO) {
                            ai.generatePCBuildXML(buildName, budget)
                        }

                        if (xmlResult.isNotBlank()) {
                            // Try to parse the new XML
                            generatedPC = parsePCBuild(xmlResult)

                            saveXmlToFile(xmlResult)
                            appSession.currentPCToBuild = generatedPC
                        }
                    } catch (e: ParseFailureException) {
                        Log.e("LoadingScreenActivity", "Attempt $attempt failed: Parsing error - ${e.message}")
                    } catch (e: Exception) {
                        Log.e("LoadingScreenActivity", "Attempt $attempt failed: ${e.message}")
                    }

                    // Short delay between attempts if needed
                    if (generatedPC == null && attempt < maxTotalAttempts) {
                        delay(1000)
                    }
                }

                if (generatedPC != null) {
                    val intent = Intent(this@LoadingScreenActivity, NewBuildSummaryActivity::class.java)
                    Log.d("LoadingScreenActivity", "Done generating PC build. Starting NewBuildSummaryActivity.")
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoadingScreenActivity", "Failed to generate valid PC build after $maxTotalAttempts attempts.")
                    Toast.makeText(
                        this@LoadingScreenActivity,
                        "Failed to generate valid build after multiple attempts. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
        else {
            appSession.currentPCToBuild = appSession.pc
            lifecycleScope.launch {
                delay(3000)
                startActivity(Intent(this@LoadingScreenActivity, NewBuildSummaryActivity::class.java))
                finish()
            }
        }

        // UI Here
        val loadingAnimation = findViewById<ImageView>(R.id.loading_animation)
        val drawable = loadingAnimation.drawable
        if (drawable is AnimatedVectorDrawable) {
            drawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    drawable?.let { (it as AnimatedVectorDrawable).start() }
                }
            })
            drawable.start()
        }

        val loadingText = findViewById<TextView>(R.id.loading_text)
        val baseText = "Building PC"
        var dotCount = 0
        lifecycleScope.launch {
            while (isActive) {
                val dots = ".".repeat(dotCount)
                loadingText.text = "$baseText$dots"
                dotCount = (dotCount + 1) % 4
                delay(500)
            }
        }

    }

    private suspend fun saveXmlToFile(xmlResult: String) {
        withContext(Dispatchers.IO) {
            // `context` is your Activity or Application context
            val fileName = "test.xml"

            val file = File(this@LoadingScreenActivity.filesDir, fileName)
            file.writeText(xmlResult)
        }
    }

    private fun Context.isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}