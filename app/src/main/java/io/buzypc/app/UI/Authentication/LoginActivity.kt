package io.buzypc.app.UI.Authentication

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import io.buzypc.app.R
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.SharedPrefManagers.BuzyAuthenticator
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.Data.SharedPrefManagers.SessionManager
import io.buzypc.app.UI.Navigation.BottomNavigationActivity
import io.buzypc.app.UI.Utils.loadCurrentUserDetails

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
        autoLogin()
        setAppTheme()

        val buzyAuthenticator = BuzyAuthenticator(this)

        val edittextUsername = findViewById<EditText>(R.id.username)
        val edittextPassword = findViewById<EditText>(R.id.password)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val btnRegister = findViewById<Button>(R.id.registerButton)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgotPassword)
        val span = SpannableString("Forgot Password?")
        span.setSpan(UnderlineSpan(), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvForgotPassword.text = span

        btnLogin.setOnClickListener {
            val enteredUsername = edittextUsername.text.toString()
            val enteredPassword = edittextPassword.text.toString()

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username and Password must not be empty",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if(!buzyAuthenticator.isUserRegistered(enteredUsername)) {
                android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.mascot_buzybee)
                    .setTitle("Profile Not Found")
                    .setMessage("Uh-oh! It seems we didn't find an existing account with this username. Create an account?")
                    .setPositiveButton("Register") { _, _ ->
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                return@setOnClickListener
            }

            if (!buzyAuthenticator.validateLogin(enteredUsername, enteredPassword)) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                val appSession = application as BuzyUserAppSession
                appSession.username = enteredUsername

                buzyAuthenticator.loginUser(enteredUsername)

                val sessionManager = SessionManager(this)
                sessionManager.createSession(appSession.username)

                setAppTheme()
                autoLogin()

                return@setOnClickListener
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            return@setOnClickListener
        }

        tvForgotPassword.setOnClickListener(){
            val buzyAuthenticator = BuzyAuthenticator(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login_activity_forgot_password, null)
            val tvValidUsername = dialogView.findViewById<TextInputEditText>(R.id.edittext_valid_username)



            val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

            val dialog = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            btnConfirm.setOnClickListener {
                if(tvValidUsername.toString().isEmpty()){
                    tvValidUsername.error = "Name can’t be empty"
                    return@setOnClickListener
                }
                if(buzyAuthenticator.isUserRegistered(tvValidUsername.text.toString())){
                    (application as BuzyUserAppSession).username = tvValidUsername.text.toString()
                    startActivity(Intent(this, ForgotPasswordActivity::class.java));
                }
                else{
                    tvValidUsername.error = "Unregistered username"
                    return@setOnClickListener
                }
                dialog.dismiss()

            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun autoLogin() {
        val sessionManager = SessionManager(this)
        if(sessionManager.isLoggedIn()) {
            val appSession = application as BuzyUserAppSession
            appSession.loadBuildList()
            val userName = sessionManager.getUsername() ?: "User"
            appSession.username = userName
            handleStartup(sessionManager)
        }
    }

    /**
     * Handles the application's startup logic based on user details and settings.
     *
     * This function determines the initial state of the application after it's launched.
     * It sets the application's theme based on the user's preferences and checks if the user is logged in.
     * If the user is logged in, it navigates them to the main application screen (BottomNavigation).
     * Otherwise, the application will remain on the current screen, likely a login or onboarding screen.
     *
     * @param userDetails An object containing details about the user, including their login status.
     * @param userSettings An object containing the user's application settings, such as the preferred theme.
     */
    private fun handleStartup(session: SessionManager) {
        if (session.isLoggedIn()) {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    /**
     * Sets the application's theme based on the user's settings.
     *
     * This function checks the user's preferred theme stored in `BuzyUserSettings`.
     * If the theme is not set or is set to "light", it sets the application theme to light mode.
     * Otherwise, it sets the application theme to dark mode.
     *
     * @param userSettings An instance of `BuzyUserSettings` containing the user's theme preference.
     *                     This object is used to both retrieve and update the user's theme setting.
     * @throws IllegalArgumentException if the theme stored in `userSettings` is neither "light" nor null, it implies an invalid configuration.
     *
     * Usage Example :
     *
     * ```kotlin
     * val userSettings = BuzyUserSettings(context)
     * setAppTheme(userSettings)
     * ```
     */
    private fun setAppTheme() {
        val userManager = loadCurrentUserDetails(this)
        val theme = userManager.getTheme()
        if (theme == null || theme == "light") {
            userManager.setTheme("light")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else {
            userManager.setTheme("dark")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
