package io.buzypc.app.UI.Navigation.Fragments.Settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import io.buzypc.app.R
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

class ProfileViewActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }

    private var isEditing = false
    private var backPressedTime = 0L

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { handleImageResult(it) }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let { handleCameraResult(it) }
    }

    private var imageProfilePicture: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleOnBackPress()
        val userDetails = loadCurrentUserDetails(this)

        val tvUsername = findViewById<TextView>(R.id.textview_username)
        tvUsername.text = userDetails.getUsername()

        val editTextEmail = findViewById<EditText>(R.id.edittext_email)
        editTextEmail.setText(userDetails.getEmail())

        val btnBackNavigation = findViewById<ImageView>(R.id.btn_back_navigation)
        val btnChangePassword = findViewById<MaterialButton>(R.id.btn_change_password)

        imageProfilePicture = findViewById(R.id.image_profile_picture)
        val imageBitmap = userDetails.getImageFromInternalStorage()
        if(imageBitmap != null) imageProfilePicture?.setImageBitmap(imageBitmap)
        else imageProfilePicture?.setImageResource(R.drawable.profilepic)

        val editProfilePicButton = findViewById<ImageButton>(R.id.btn_edit_profile_picture)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        editProfilePicButton.setOnClickListener {
            showChangePhotoDialog()
            return@setOnClickListener
        }

        btnBackNavigation.setOnClickListener {
            finish()
            return@setOnClickListener
        }

        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
            return@setOnClickListener
        }

        btnEditProfile.setOnClickListener {
            isEditing = !isEditing

            if (isEditing) {
                editTextEmail.isEnabled = true
                editProfilePicButton.visibility = View.VISIBLE
                editTextEmail.setBackgroundResource(R.drawable.edit_text_border)
                btnEditProfile.text = "Save Profile"

                btnBackNavigation.visibility = View.GONE

                btnEditProfile.setCompoundDrawablesWithIntrinsicBounds( R.drawable.save_24px, 0, 0, 0)
                Toast.makeText(this, "Now editing user profile", Toast.LENGTH_LONG).show()
            } else {
                val updatedEmail = editTextEmail.text.toString().trim()

                if(validate(updatedEmail, editTextEmail)) {
                    editTextEmail.setText(updatedEmail)

                    editTextEmail.isEnabled = false
                    editProfilePicButton.visibility = View.GONE
                    editTextEmail.background = null
                    editTextEmail.setPadding(0,0,0,0)
                    btnEditProfile.text = "Edit Profile"

                    btnBackNavigation.visibility = View.VISIBLE

                    btnEditProfile.setCompoundDrawablesWithIntrinsicBounds( R.drawable.baseline_edit_24, 0, 0, 0)

                    userDetails.saveProfile(updatedEmail)
                    Toast.makeText(this, "User profile has been saved", Toast.LENGTH_SHORT).show()

                    isEditing = false
                }
                else {
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnClickListener
        }
    }

    private fun handleOnBackPress() {

        onBackPressedDispatcher.addCallback(this@ProfileViewActivity, object : OnBackPressedCallback(true) {
            val editTextEmail = findViewById<EditText>(R.id.edittext_email)
            val editProfilePicButton = findViewById<ImageButton>(R.id.btn_edit_profile_picture)
            val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)
            val btnBackNavigation = findViewById<ImageView>(R.id.btn_back_navigation)
            val userDetails = loadCurrentUserDetails(this@ProfileViewActivity)

            override fun handleOnBackPressed() {
                if(!isEditing) {
                    finish()
                    return
                }

                if (backPressedTime + 2000 > System.currentTimeMillis()) {

                    val updatedEmail = editTextEmail.text.toString().trim()

                    if(validate(updatedEmail, editTextEmail)) {
                        editTextEmail.setText(updatedEmail)

                        editTextEmail.isEnabled = false
                        editProfilePicButton.visibility = View.GONE
                        editTextEmail.background = null
                        editTextEmail.setPadding(0,0,0,0)
                        btnEditProfile.text = "Edit Profile"

                        btnBackNavigation.visibility = View.VISIBLE

                        btnEditProfile.setCompoundDrawablesWithIntrinsicBounds( R.drawable.baseline_edit_24, 0, 0, 0)

                        userDetails.saveProfile(updatedEmail)
                        Toast.makeText(this@ProfileViewActivity, "User profile has been saved", Toast.LENGTH_SHORT).show()

                        isEditing = false
                    }
                    else {
                        Toast.makeText(this@ProfileViewActivity, "Invalid email", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@ProfileViewActivity, "Currently editing profile. Press back again to save.", Toast.LENGTH_SHORT).show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        })
    }

    /**
     * Displays a dialog for the user to change their password.
     *
     * This function inflates a layout containing input fields for the current password,
     * new password, and confirmation of the new password. It then presents this layout
     * in a MaterialAlertDialog.
     *
     * When the user clicks the "Confirm" button, the function validates the entered passwords.
     * If validation passes, it updates the user's password (in this example, a mock update)
     * and displays a success message.
     * If the user clicks "Cancel," the dialog is simply dismissed.
     *
     * The dialog is non-cancelable by clicking outside of it.
     *
     */
    private fun showChangePasswordDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profile_view_change_password, null)
        val currentPasswordInput = dialogView.findViewById<TextInputEditText>(R.id.edittext_old_password)
        val newPasswordInput = dialogView.findViewById<TextInputEditText>(R.id.edittext_new_password)
        val confirmNewPasswordInput = dialogView.findViewById<TextInputEditText>(R.id.edittext_confirm_password)

        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnConfirm.setOnClickListener {
            val currentPassword = currentPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmNewPasswordInput.text.toString()

            if (validatePasswordChange(currentPassword, newPassword, confirmPassword)) {
                val userDetails = loadCurrentUserDetails(this)
                userDetails.setPassword(newPassword)
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showChangePhotoDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_profile_pic_choice, null)

        // Initialize buttons
        val btnCamera = dialogView.findViewById<MaterialButton>(R.id.btnCamera)
        val btnGallery = dialogView.findViewById<MaterialButton>(R.id.btnGallery)

        // Create the dialog
        val dialog = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Button click handlers
        btnCamera.setOnClickListener {
            launchCamera()
            dialog.dismiss()
        }

        btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleImageResult(uri: Uri) {
        val userDetails = loadCurrentUserDetails(this)
        val bitmap = uriToBitmap(this, uri)
        userDetails.saveImageToInternalStorage(this, bitmap, "profile_pic.png")
        imageProfilePicture?.setImageBitmap(bitmap)
    }

    private fun handleCameraResult(bitmap: Bitmap) {
        val userDetails = loadCurrentUserDetails(this)
        userDetails.saveImageToInternalStorage(this, bitmap, "profile_pic.png")
        imageProfilePicture?.setImageBitmap(bitmap)
    }

    private fun launchCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(null)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Validates a password change request.
     *
     * This function checks if the provided current password matches the user's stored password,
     * and if the new password and its confirmation match each other.
     * If either of these checks fail, an appropriate error message is displayed via a Toast.
     *
     * @param currentPassword The user's current password.
     * @param newPassword The new password the user wants to set.
     * @param confirmPassword The confirmation of the new password.
     * @return `true` if the password change request is valid, `false` otherwise.
     *
     * **Error Handling:**
     * - Displays a "Incorrect current password!" Toast if the `currentPassword` is incorrect.
     * - Displays a "New passwords do not match!" Toast if the `newPassword` and `confirmPassword` do not match.
     *
     * **Side Effects:**
     * - Displays Toast messages to the user in case of validation failures.
     * - Loads user details using `loadCurrentUserDetails(this)`.
     * - Validates password using user details object `userDetails.validatePassword(currentPassword)`.
     *
     * **Preconditions:**
     * - `loadCurrentUserDetails(this)` should return a valid UserDetails object.
     * - The UserDetails object should have a `validatePassword` method to check passwords.
     *
     * **Postconditions:**
     * - If the function returns true, it indicates that the user entered correct information and the password change can proceed.
     * - If the function returns false, it means there is an error with the current password or mismatch with the new password and the process should stop.
     */
    private fun validatePasswordChange(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
        val userDetails = loadCurrentUserDetails(this)
        if(!userDetails.validatePassword(currentPassword)) {
            Toast.makeText(this, "Incorrect current password!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(newPassword.length < 6) {
            Toast.makeText(this, "Password must have at least 6 characters!", Toast.LENGTH_LONG).show()
            return false
        }
        if(newPassword != confirmPassword) {
            Toast.makeText(this, "New passwords do not match!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /**
     * Converts a URI to a Bitmap.
     *
     * This function takes a content URI and returns a corresponding Bitmap object.
     * It handles different Android versions using the appropriate API:
     * - For Android Pie (API 28) and above, it uses `ImageDecoder` for efficient decoding.
     * - For versions below Android Pie, it falls back to the deprecated `MediaStore.Images.Media.getBitmap`.
     *
     * @param context The application context. Required to access the content resolver.
     * @param uri The URI of the image to decode.
     * @return The decoded Bitmap, or throws an exception if the URI is invalid or decoding fails.
     * @throws IllegalArgumentException if the provided URI is null or invalid.
     * @throws java.io.IOException if there is an error reading the image data from the URI.
     */
    private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    /**
     * Validates the provided name and email strings against predefined rules.
     *
     * This function checks if the name and email fields are valid based on the following criteria:
     * - **Name:**
     *   1. Cannot be empty or contain only whitespace.
     *   2. Must contain only letters (A-Z, a-z) and spaces.
     * - **Email:**
     *   1. Cannot be empty or contain only whitespace.
     *   2. Must adhere to a valid email format.
     *
     * If a validation rule is violated, the corresponding EditText will display an error message.
     *
     * @param email The email string to validate.
     * @param editTextEmail The EditText field associated with the email, used for displaying error messages.
     * @return `true` if both the name and email are valid, `false` otherwise.
     */
    private fun validate(email: String, editTextEmail: EditText) : Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS

        return when {
            // isBlank() returns true if the string is empty or only contains whitespace characters
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