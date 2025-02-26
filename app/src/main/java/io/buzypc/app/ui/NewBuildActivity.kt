package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R

class NewBuildActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_build)
        val profileViewButton = findViewById<ImageView>(R.id.image_profileButton)

        profileViewButton.setOnClickListener(){
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}