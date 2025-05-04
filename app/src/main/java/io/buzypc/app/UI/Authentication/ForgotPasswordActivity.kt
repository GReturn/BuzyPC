package io.buzypc.app.UI.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserManager
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newPassword = findViewById<TextInputEditText>(R.id.newPassword)
        val confirmNewPassword = findViewById<TextInputEditText>(R.id.confirmNewPassword)
        val btnSavePassword = findViewById<MaterialButton>(R.id.btn_save_password)
        val app = application as BuzyUserAppSession
        val passwordMinLength = 6

        btnSavePassword.setOnClickListener(){
            if(newPassword.text.toString().isEmpty() || confirmNewPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Input all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(newPassword.text.toString() != confirmNewPassword.text.toString()){
                Toast.makeText(this, "Password is not matched", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(newPassword.text.toString().length < passwordMinLength){
                Toast.makeText(this,"Password must be at least 6 characters long", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val userDetails = loadCurrentUserDetails(this)
            userDetails.setPassword(newPassword.text.toString())
            app.username = ""
            Toast.makeText(this, "Password has been changed", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))

        }

    }
}