package io.buzypc.app.UI.Authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.Data.SharedPrefManagers.BuzyAuthenticator
import io.buzypc.app.Data.SharedPrefManagers.UserRegistryManager
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

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

        edittextUsername.addTextChangedListener( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length == 20) Toast.makeText(this@RegisterActivity, "Only 20 characters allowed for the username.", Toast.LENGTH_LONG).show()
            }
        })

        btnRegister.setOnClickListener {
            val buzyAuthenticator = BuzyAuthenticator(this)

            val nameRegex = "^[A-Za-z ]{3,20}$".toRegex()
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS
            val passwordMinLength = 6

            // username
            if(edittextUsername.text.isNullOrEmpty() || edittextEmail.text.isNullOrEmpty() ||
                edittextPassword.text.isNullOrEmpty() || edittextConfirmPassword.text.isNullOrEmpty()){
                Toast.makeText(this,"Fill out all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(buzyAuthenticator.isUserRegistered(edittextUsername.text.toString())) {
                Toast.makeText(this,"This username is already taken", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!edittextUsername.text.matches(nameRegex)){
                Toast.makeText(this,"Name should contain 3-20 alphabetical letters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // email
            else if(!emailPattern.matcher(edittextEmail.text).matches()){
                Toast.makeText(this,"Invalid Email Address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // password
            else if(edittextPassword.text.length < passwordMinLength){
                Toast.makeText(this,"Password must be at least 6 characters long", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(!edittextPassword.text.contentEquals(edittextConfirmPassword.text)){
                Toast.makeText(this,"Passwords do not match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            else{
                val username = edittextUsername.text.toString()
                val email = edittextEmail.text.toString()
                val password = edittextPassword.text.toString()
                buzyAuthenticator.registerUser(username, email, password)

                Toast.makeText(this, "Account successfully created!", Toast.LENGTH_LONG).show()
                intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}