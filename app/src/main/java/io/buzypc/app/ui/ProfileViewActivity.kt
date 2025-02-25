package io.buzypc.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.BuzyUser

class ProfileViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userDetails = BuzyUser(this)
        val txtUsername = findViewById<TextView>(R.id.textView_usernameDisplay)
        txtUsername.text = userDetails.getUsername()

        val btnBackNavigation = findViewById<ImageView>(R.id.image_backNavigation)
        btnBackNavigation.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        val buttonLogout = findViewById<Button>(R.id.btn_logout)
        buttonLogout.setOnClickListener {
            Log.e("USER_LOGOUT","Logout button is clicked")
            Toast.makeText(this, "Logout button is clicked", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LogoutPromptActivity::class.java)
            startActivity(intent)
        }

        val buttonSettings = findViewById<Button>(R.id.btn_settings)
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val buttonAboutDevelopers = findViewById<Button>(R.id.btn_aboutDevelopers)
        buttonAboutDevelopers.setOnClickListener {
            val intent = Intent(this, AboutDevelopersActivity::class.java)
            startActivity(intent)
        }
    }

}