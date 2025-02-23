package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.BuzyUser


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userDetails = BuzyUser(this)

        val edittext_username = findViewById<EditText>(R.id.username)
        val edittext_password = findViewById<EditText>(R.id.password)

        val button_login = findViewById<Button>(R.id.loginButton)
        val button_register = findViewById<Button>(R.id.registerButton)

        button_login.setOnClickListener {
            if(!userDetails.isUserRegistered()) {
                AlertDialog.Builder(this)
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

            if(edittext_username.text.isNullOrEmpty() || edittext_password.text.isNullOrEmpty()){
                Toast.makeText(this,"Username and Password must not be empty",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(userDetails.validateLogin(edittext_username.text.toString(), edittext_password.text.toString())) {
                val intent = Intent(this, LandingPageActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show()
            }
        }

        button_register.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}