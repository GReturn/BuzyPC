package io.buzypc.app.UI.Navigation.Fragments.Settings

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.R

class AboutDevelopersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about_developers)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack = findViewById<Button>(R.id.btn_back_navigation)
        btnBack.setOnClickListener {
            finish()
        }

        val imgRafael = findViewById<ImageView>(R.id.image_rafael)
        imgRafael.setOnClickListener {
            val app = applicationContext as BuzyUserAppSession
            app.useAI = app.useAI.not()
            Toast.makeText(this, "AI is now ${if (app.useAI) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
    }
}