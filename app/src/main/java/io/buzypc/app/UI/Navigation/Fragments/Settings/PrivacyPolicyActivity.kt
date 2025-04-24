package io.buzypc.app.UI.Navigation.Fragments.Settings

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy_policy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btn_back_navigation)
        btnBack.setOnClickListener {
            finish()
        }

        val htmlPrivacyPolicy =
            """
                <h2>Privacy Policy</h2>
                <p><i>Last updated: April 24, 2025</i></p>
            
                <p>Welcome to <b>BuzyPC</b> — your budget-friendly PC build assistant!</p>
            
                <p>This privacy policy is here to let you know what kind of information we collect and how we use it.
                Don't worry — we keep it simple and respect your privacy.</p>
            
                <h3>1. Information We Collect</h3>
                <p>BuzyPC does <b>not collect any personal data</b>. All the data (like your PC build preferences, budget,
                or checklist items) stays <b>locally on your device</b>.</p>
            
                <h3>2. How We Use Your Data</h3>
                <p>Your input is used only to generate and manage your custom PC builds. That’s it.
                No tracking, no analytics, no funny business.</p>
            
                <h3>3. Data Sharing</h3>
                <p>We don’t share, sell, or send your data anywhere. Your builds are <b>yours alone</b>.</p>
            
                <h3>4. Internet Access</h3>
                <p>BuzyPC may access the internet <b>only</b> for optional features like fetching component suggestions or
                updates in the future. These are strictly for app enhancement.</p>
            
                <h3>5. Changes to This Policy</h3>
                <p>This is a mock privacy policy for demonstration purposes. In a real deployment, we’d notify users if
                anything changed.</p>
            
                <h3>Contact</h3>
                <p>Questions or concerns? Feel free to reach out when we’re real. 😉<br>
                For now, enjoy building your dream PC with <b>BuzyPC</b>.</p>
            """.trimIndent()

        val privacyPolicyTextView = findViewById<TextView>(R.id.privacy_policy_textview)
        privacyPolicyTextView.text = HtmlCompat.fromHtml(htmlPrivacyPolicy, HtmlCompat.FROM_HTML_MODE_LEGACY)

    }
}