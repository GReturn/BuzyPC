package io.buzypc.app.data.user

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.buzypc.app.data.appsession.BuzyUserAppSession
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

class BuzyUser(private val context: Context) {
    var buildNameList = ArrayList<String>()
    var buildBudgetList = ArrayList<String>()

    private lateinit var sharedPreferences: SharedPreferences

    init {
        loadUser()
    }
    // region User Profile Authentication functions

    fun registerUser(username: String, email: String, password: String) {
        sharedPreferences = context.getSharedPreferences("buzy-user-secrets_${username}", Context.MODE_PRIVATE)
        saveProfile(username, email, password)
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }

    /**
     * For first time registration;
     * Saves user profile information (username, email, and a hashed password) to shared preferences.
     *
     * This function takes the user's username, email, and password as input. It then hashes the
     * password for security before storing it. The username, email, and the hashed password are
     * stored persistently in the application's shared preferences.
     *
     * @param username The user's username.
     * @param email The user's email address.
     * @param password The user's password (will be hashed before storage).
     */
    private fun saveProfile(username: String, email: String, password: String) {
        val hashedPassword = hashPassword(password)
        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .putString("password", hashedPassword)
            .apply()
    }

    /**
     * Saves the user's profile information (username and email) to SharedPreferences.
     *
     * This function utilizes SharedPreferences to persistently store the provided username and email.
     * These values can be retrieved later using the corresponding keys ("username" and "email").
     * The data is saved asynchronously using `apply()`.
     *
     * @param username The username of the user to be saved.
     * @param email The email address of the user to be saved.
     */
    fun saveProfile(username: String, email: String) {
        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .apply()
    }

    /**
     * For logging in;
     * Loads the SharedPreferences for a specific user.
     *
     * This function initializes the SharedPreferences instance associated with the given username.
     * The SharedPreferences file is named "buzy-user-secrets_{username}" and is accessible
     * only by this application (Context.MODE_PRIVATE).  It is intended to store user-specific
     * secrets or preferences.
     *
     * @param username The username of the user whose SharedPreferences should be loaded.
     *                 This string is used to create a unique SharedPreferences file name for each user.
     * @throws IllegalArgumentException if the username is blank or empty.
     *
     * Example Usage:
     * ```
     * // Assuming 'context' is a valid Android Context object
     * loadUser("johndoe") // Loads the preferences for user "johndoe"
     * loadUser("janedoe") // loads the preference for user "janedoe"
     * ```
     */
    fun loadUser(username: String) {
        sharedPreferences = context.getSharedPreferences("buzy-user-secrets_${username}", Context.MODE_PRIVATE)
    }

    /**
     * Generally for loading current user data;
     * Loads user-specific shared preferences.
     *
     * This function retrieves the username from the BuzyUserAppSession and then loads
     * the SharedPreferences associated with that user. The SharedPreferences are used
     * to store user-specific data, and the name of the preferences file is
     * constructed as "buzy-user-secrets_{username}".
     *
     * The SharedPreferences are accessed in private mode (Context.MODE_PRIVATE),
     * meaning they can only be accessed by this application.
     *
     * @throws IllegalStateException If the context is not properly initialized.
     *
     * @see BuzyUserAppSession
     * @see SharedPreferences
     * @see Context.getSharedPreferences
     * @see Context.MODE_PRIVATE
     */
    fun loadUser() {
        val username = BuzyUserAppSession().username
        sharedPreferences = context.getSharedPreferences("buzy-user-secrets_${username}", Context.MODE_PRIVATE)
    }

    // endregion

    fun isUserRegistered(username: String): Boolean = username == getUsername()

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

    fun getUsername(): String? = sharedPreferences.getString("username", null)

    fun getEmail(): String? = sharedPreferences.getString("email", null)

    fun getContactNumber(): String? = sharedPreferences.getString("contactNumber", null)

    fun setUsername(newName: String) = sharedPreferences.edit().putString("username", newName).apply()

    fun setEmail(newEmail: String) = sharedPreferences.edit().putString("email", newEmail).apply()

    fun setContactNumber(newContactNumber: String) = sharedPreferences.edit().putString("contactNumber", newContactNumber).apply()

    fun setPassword(newPassword: String) = sharedPreferences.edit().putString("password", hashPassword(newPassword)).apply()

    fun logout() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun validatePassword(inputPassword: String) : Boolean {
        val realPassword = sharedPreferences.getString("password", null)
        return realPassword == hashPassword(inputPassword)
    }

    fun validateLogin(inputUsername: String, inputPassword: String) : Boolean {
        val realUsername = getUsername()
        val realPassword = sharedPreferences.getString("password", null)

        return if (realUsername == inputUsername && realPassword == hashPassword(inputPassword)) {
            /*
                commit() is synchronous, so it blocks the process (apply is async).
                This is ideal for our use-case since we don't want the user to log in
                before the login state of user is written to file. If commit() was
                unsuccessful, it will return false.
            */
            sharedPreferences.edit().putBoolean("isLoggedIn", true).commit()
            true
        }
        else {
            false
        }
    }

    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, filename: String) {
        // let's personalize each profile image file name for easier identification
        val trueImageFilename = filename + getUsername()

        val file = File(context.filesDir, trueImageFilename)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        sharedPreferences.edit().putString("profile_pic",file.absolutePath).apply()
    }

    fun getImageFromInternalStorage(): Bitmap? {
        val filename = sharedPreferences.getString("profile_pic", null)
        return filename?.let { BitmapFactory.decodeFile(it) }
    }

    private fun hashPassword(password: String) : String {
        // Message Digest: https://www.geeksforgeeks.org/message-digest-in-information-security/
        // List of algorithms: https://developer.android.com/reference/java/security/MessageDigest
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

   // TODO: Temporary; we should store build information as a JSON or XML string

    fun saveBuilds() {
        sharedPreferences.edit()
            .putString("buildNameList", buildNameList.joinToString(separator = ","))
            .putString("buildBudgetList", buildBudgetList.joinToString(separator = ","))
            .apply()
    }

    fun retrieveBuilds() {
        val savedNames = sharedPreferences.getString("buildNameList", "") ?: ""
        val savedBudgets = sharedPreferences.getString("buildBudgetList", "") ?: ""
        buildNameList.clear()
        buildBudgetList.clear()
        if (savedNames.isNotEmpty()) {
            buildNameList.addAll(savedNames.split(","))
        }
        if (savedBudgets.isNotEmpty()) {
            buildBudgetList.addAll(savedBudgets.split(","))
        }
    }
}