package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val button_login = findViewById<Button>(R.id.loginButton)
        val button_register = findViewById<Button>(R.id.registerButton)

        val edittext_username = findViewById<EditText>(R.id.registerUsername)
        val edittext_email = findViewById<EditText>(R.id.emailRegister)
        val edittext_password = findViewById<EditText>(R.id.passwordRegister)
        val edittext_confirmPassword = findViewById<EditText>(R.id.confirmPasswordRegister)

        button_login.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        button_register.setOnClickListener(){
            if(edittext_username.text.isNullOrEmpty() || edittext_email.text.isNullOrEmpty() ||
                edittext_password.text.isNullOrEmpty() || edittext_confirmPassword.text.isNullOrEmpty()){
                Toast.makeText(this,"Fill out all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!edittext_password.text.contentEquals(edittext_confirmPassword.text)){
                Toast.makeText(this,"Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edittext_email.text).matches()){
                Toast.makeText(this,"Invalid Email Address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                intent = Intent(this, LandingPageActivity::class.java)
                startActivity(intent)
            }
        }
    }
}