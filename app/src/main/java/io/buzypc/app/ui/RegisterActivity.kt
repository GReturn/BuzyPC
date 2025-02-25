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
import io.buzypc.app.data.user.BuzyUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogin = findViewById<Button>(R.id.loginButton)
        val btnRegister = findViewById<Button>(R.id.registerButton)

        val edittextUsername = findViewById<EditText>(R.id.edittext_username)
        val edittextEmail = findViewById<EditText>(R.id.edittext_email)
        val edittextPassword = findViewById<EditText>(R.id.edittext_password)
        val edittextConfirmPassword = findViewById<EditText>(R.id.edittext_confirm_password)

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val nameRegex = "^[A-Za-z ]+$".toRegex()
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS

            if(edittextUsername.text.isNullOrEmpty() || edittextEmail.text.isNullOrEmpty() ||
                edittextPassword.text.isNullOrEmpty() || edittextConfirmPassword.text.isNullOrEmpty()){
                Toast.makeText(this,"Fill out all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!edittextUsername.text.matches(nameRegex)){
                Toast.makeText(this,"Name can only contain letters and spaces", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!edittextPassword.text.contentEquals(edittextConfirmPassword.text)){
                Toast.makeText(this,"Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            else if(!emailPattern.matcher(edittextEmail.text).matches()){
                Toast.makeText(this,"Invalid Email Address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                val userDetails = BuzyUser(this)

                userDetails.saveProfile(
                        edittextUsername.text.toString(),
                        edittextEmail.text.toString(),
                        edittextPassword.text.toString()
                )
                intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}