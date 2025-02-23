package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R

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
        val btnBackNavigation = findViewById<ImageView>(R.id.image_backNavigation)
        btnBackNavigation.setOnClickListener {
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        }

        val radioButtonLightMode = findViewById<RadioButton>(R.id.rb_lightMode)
        val radioButtonDarkMode = findViewById<RadioButton>(R.id.rb_darkMode)

        radioButtonLightMode.isChecked = true;
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
//            radioButtonDarkMode.isChecked = true;
//        }
        radioButtonLightMode.setOnClickListener {
            if(radioButtonLightMode.isChecked) {
                radioButtonDarkMode.isChecked = false

//                setTheme(R.style.Base_Theme_BuzyPC_Light)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                recreate()
            }
        }
        radioButtonDarkMode.setOnClickListener {
            if(radioButtonDarkMode.isChecked) {
                radioButtonLightMode.isChecked = false

//                setTheme(R.style.Base_Theme_BuzyPC_Dark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                recreate()
            }
        }
//        radioButtonCreamMode.setOnClickListener {
//            if(radioButtonCreamMode.isChecked) {
//                radioButtonLightMode.isChecked = false
//                radioButtonDarkMode.isChecked = false
//
//                setTheme(R.style.Base_Theme_BuzyPC_Cream)
//                recreate()
//            }
//        }


    }
}