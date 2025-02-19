package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R

class LogoutPromptActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logout_prompt)

        val button_logout = findViewById<Button>(R.id.confirmLogoutButton)
        val button_cancel = findViewById<Button>(R.id.cancel_button)
        button_logout.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        button_cancel.setOnClickListener(){
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        }
    }
}