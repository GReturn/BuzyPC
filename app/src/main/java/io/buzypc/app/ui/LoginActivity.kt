package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.user.BuzyUser
import io.buzypc.app.data.user.BuzyUserSettings


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userSettings = BuzyUserSettings(this)
        if(userSettings.getTheme() == null || userSettings.getTheme() == "light") {
            userSettings.setTheme("light")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val userDetails = BuzyUser(this)

        val edittextUsername = findViewById<EditText>(R.id.username)
        val edittextPassword = findViewById<EditText>(R.id.password)

        val btnLogin = findViewById<Button>(R.id.loginButton)
        val btnRegister = findViewById<Button>(R.id.registerButton)

        btnLogin.setOnClickListener {
            if(!userDetails.isUserRegistered()) {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.buzybee)
                    .setTitle("Profile Not Found")
                    .setMessage("Uh-oh! It seems we didn't find an existing account. Create an account?")
                    .setPositiveButton("Register") { _,_ ->
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                return@setOnClickListener
            }

            if(edittextUsername.text.isNullOrEmpty() || edittextPassword.text.isNullOrEmpty()){
                Toast.makeText(this,"Username and Password must not be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(userDetails.validateLogin(edittextUsername.text.toString(), edittextPassword.text.toString())) {
                val intent = Intent(this, LandingPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}