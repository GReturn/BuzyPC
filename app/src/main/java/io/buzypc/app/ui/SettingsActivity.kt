package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.BuzyUser
import io.buzypc.app.data.BuzyUserSettings

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userDetails = BuzyUser(this)
        val btnBackNavigation = findViewById<ImageView>(R.id.image_backNavigation)
        val txtUsername = findViewById<TextView>(R.id.textView_usernameDisplay)

        val buttonEditAccount = findViewById<Button>(R.id.btn_edit_account)
        val buttonAboutDevelopers = findViewById<Button>(R.id.btn_about_developers)

        val radioButtonLightMode = findViewById<RadioButton>(R.id.rb_lightMode)
        val radioButtonDarkMode = findViewById<RadioButton>(R.id.rb_darkMode)

        val userSettings = BuzyUserSettings(this)
        if(userSettings.getTheme() == "light" || userSettings.getTheme() == null)
            radioButtonLightMode.isChecked = true
        else radioButtonDarkMode.isChecked = true

        val buttonLogout = findViewById<Button>(R.id.btn_logout)

        txtUsername.text = userDetails.getUsername()

        btnBackNavigation.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        buttonEditAccount.setOnClickListener {
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        }

        buttonAboutDevelopers.setOnClickListener {
            val intent = Intent(this, AboutDevelopersActivity::class.java)
            startActivity(intent)
        }

        radioButtonLightMode.setOnClickListener {
            if(radioButtonLightMode.isChecked) {
                radioButtonDarkMode.isChecked = false
                userSettings.setTheme("light")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        radioButtonDarkMode.setOnClickListener {
            if(radioButtonDarkMode.isChecked) {
                radioButtonLightMode.isChecked = false
                userSettings.setTheme("dark")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        buttonLogout.setOnClickListener {
            Log.e("USER_LOGOUT","Logout button is clicked")
            Toast.makeText(this, "Logout button is clicked", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LogoutPromptActivity::class.java)
            startActivity(intent)
        }
    }
}