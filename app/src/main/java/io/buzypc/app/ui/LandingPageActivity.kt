package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.BuzyUser

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userDetails = BuzyUser(this)
        val txtHelloUser = findViewById<TextView>(R.id.hello_user)
        txtHelloUser.text = getString(R.string.hello_user, userDetails.getUsername())

        val btnAdd = findViewById<ImageView>(R.id.add_build)
        btnAdd.setOnClickListener {
            val intent = Intent(this,NewBuildActivity::class.java)
            startActivity(intent)
        }

        val btnCartButton = findViewById<ImageView>(R.id.view_cart)
        btnCartButton.setOnClickListener(){
            val intent = Intent(this, BuildsListActivity::class.java)
            startActivity(intent)
        }
    }
}