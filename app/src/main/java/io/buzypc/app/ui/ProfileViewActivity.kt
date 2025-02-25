package io.buzypc.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.buzypc.app.R
import io.buzypc.app.data.user.BuzyUser

class ProfileViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userDetails = BuzyUser(this)
        val editTextUsername = findViewById<EditText>(R.id.edittext_username)
        editTextUsername.setText(userDetails.getUsername())
        val editTextEmail = findViewById<EditText>(R.id.edittext_email)
        editTextEmail.setText(userDetails.getEmail())

        val editProfilePicButton = findViewById<ImageButton>(R.id.btn_edit_profile_picture)
        val btnBackNavigation = findViewById<ImageView>(R.id.image_backNavigation)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)


        btnBackNavigation.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btnEditProfile.setOnClickListener {
            val isEditing = editTextUsername.isEnabled
            if (!isEditing) {
                editTextUsername.isEnabled = true
                editTextEmail.isEnabled = true
                editProfilePicButton.visibility = View.VISIBLE
                editTextUsername.setBackgroundResource(R.drawable.edit_text_border)
                editTextEmail.setBackgroundResource(R.drawable.edit_text_border)
                btnEditProfile.text = "Save Profile"

                btnEditProfile.setCompoundDrawablesWithIntrinsicBounds( R.drawable.save_24px, 0, 0, 0)
                Toast.makeText(this, "Now editing user profile", Toast.LENGTH_LONG).show()
            } else {
                val updatedUsername = editTextUsername.text.toString().trim()
                val updatedEmail = editTextEmail.text.toString().trim()

                if(validate(updatedUsername, updatedEmail, editTextUsername, editTextEmail)) {
                    editTextUsername.setText(updatedUsername)
                    editTextEmail.setText(updatedEmail)

                    editTextUsername.isEnabled = false
                    editTextEmail.isEnabled = false
                    editProfilePicButton.visibility = View.GONE
                    editTextUsername.background = null
                    editTextEmail.background = null
                    editTextUsername.setPadding(0,0,0,0)
                    editTextEmail.setPadding(0,0,0,0)
                    btnEditProfile.text = "Edit Profile"

                    btnEditProfile.setCompoundDrawablesWithIntrinsicBounds( R.drawable.baseline_edit_24, 0, 0, 0)

                    userDetails.saveProfile(updatedUsername, updatedEmail)
                    Toast.makeText(this, "User profile has been saved", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validate(name: String, email: String, editTextUsername: EditText, editTextEmail: EditText) : Boolean {
        val nameRegex = "^[A-Za-z ]+$".toRegex()
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS

        return when {
            // isBlank() returns true if the string is empty or only contains whitespace characters
            name.isBlank() -> {
                editTextUsername.error = "Name cannot be empty"
                false
            }
            !name.matches(nameRegex) -> {
                editTextUsername.error = "Name can only contain letters and spaces"
                false
            }
            email.isBlank() -> {
                editTextEmail.error = "Email cannot be empty"
                false
            }
            !emailPattern.matcher(email).matches() -> {
                editTextEmail.error = "Invalid email format"
                false
            }
            else -> true
        }
    }
}