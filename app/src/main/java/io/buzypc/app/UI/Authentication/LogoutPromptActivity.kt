package io.buzypc.app.UI.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.Data.SharedPrefManagers.BuzyAuthenticator
import io.buzypc.app.Data.SharedPrefManagers.SessionManager
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

class LogoutPromptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout_prompt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogout = findViewById<Button>(R.id.confirmLogoutButton)
        val btnCancelLogout = findViewById<Button>(R.id.cancel_button)

        btnLogout.setOnClickListener {
            val buzyAuthenticator = BuzyAuthenticator(this)
            buzyAuthenticator.logout()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        btnCancelLogout.setOnClickListener {
            finish()
        }
    }
}