package io.buzypc.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //hello first comment
        setContentView(R.layout.activity_main) // comment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    fun applyTheme() {
        // well handle some stuff for changing themes based on saved file; for now, this will do
        setTheme(R.style.Theme_Light)
    }
}