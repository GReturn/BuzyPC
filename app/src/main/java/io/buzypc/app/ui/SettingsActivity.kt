package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.user.BuzyUser
import io.buzypc.app.data.user.BuzyUserSettings

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

        val imageProfilePicture = findViewById<ImageView>(R.id.image_profile_picture)
        val imageBitmap = userDetails.getImageFromInternalStorage()
        if(imageBitmap != null) imageProfilePicture.setImageBitmap(imageBitmap)
        else imageProfilePicture.setImageResource(R.drawable.profilepic)


        val btnEditAccount = findViewById<Button>(R.id.btn_edit_account)
        val btnAboutDevelopers = findViewById<Button>(R.id.btn_about_developers)

        val radioBtnLightMode = findViewById<RadioButton>(R.id.rb_lightMode)
        val radioBtnDarkMode = findViewById<RadioButton>(R.id.rb_darkMode)
        val cardLightMode = findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_lightMode)
        val cardDarkMode = findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_darkMode)
        cardLightMode.strokeWidth = 0
        cardDarkMode.strokeWidth = 0

        val userSettings = BuzyUserSettings(this)
        if(userSettings.getTheme() == "light" || userSettings.getTheme() == null) {
            radioBtnLightMode.isChecked = true
            cardLightMode.strokeWidth = 3
            cardDarkMode.strokeWidth = 0
        }
        else {
            radioBtnDarkMode.isChecked = true
            cardLightMode.strokeWidth = 0
            cardDarkMode.strokeWidth = 3
        }

        val buttonLogout = findViewById<Button>(R.id.btn_logout)

        txtUsername.text = userDetails.getUsername()

        btnBackNavigation.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            startActivity(intent)
        }

        btnEditAccount.setOnClickListener {
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        }

        btnAboutDevelopers.setOnClickListener {
            val intent = Intent(this, AboutDevelopersActivity::class.java)
            startActivity(intent)
        }

        radioBtnLightMode.setOnClickListener {
            if(radioBtnLightMode.isChecked) {
                radioBtnDarkMode.isChecked = false
                userSettings.setTheme("light")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                cardLightMode.strokeWidth = 3
                cardDarkMode.strokeWidth = 0
            }
        }
        radioBtnDarkMode.setOnClickListener {
            if(radioBtnDarkMode.isChecked) {
                radioBtnLightMode.isChecked = false
                userSettings.setTheme("dark")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                cardLightMode.strokeWidth = 0
                cardDarkMode.strokeWidth = 3
            }
        }

        buttonLogout.setOnClickListener {
            val intent = Intent(this, LogoutPromptActivity::class.java)
            startActivity(intent)
        }
    }
}