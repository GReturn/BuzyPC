package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.SharedPrefManagers.Util.hashPassword
import java.io.File
import java.io.FileOutputStream

class BuzyUserManager(private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences
    private val SHARED_PREF_FILENAME_TEMPLATE = "buzy-user-secrets_"

    init {
        loadUser()
    }
    // region User Profile Authentication functions

    fun registerUser(username: String, email: String, password: String) {
        sharedPreferences = context.getSharedPreferences("${SHARED_PREF_FILENAME_TEMPLATE}${username}", Context.MODE_PRIVATE)

        val hashedPassword = hashPassword(password)
        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .putString("password", hashedPassword)
            .apply()
    }

    /**
     * Saves the user's profile information (email) to SharedPreferences.
     *
     * This function utilizes SharedPreferences to persistently store the provided email.
     * These values can be retrieved later using the corresponding keys ("email").
     * The data is saved asynchronously using `apply()`.
     *
     * @param email The email address of the user to be saved.
     */
    fun saveProfile(email: String) {
        sharedPreferences.edit()
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
        sharedPreferences = context.getSharedPreferences("${SHARED_PREF_FILENAME_TEMPLATE}${username}", Context.MODE_PRIVATE)
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
    private fun loadUser() {
        val username = BuzyUserAppSession().username
        sharedPreferences = context.getSharedPreferences("${SHARED_PREF_FILENAME_TEMPLATE}${username}", Context.MODE_PRIVATE)
    }

    // endregion

    fun getUsername(): String? = sharedPreferences.getString("username", null)

    fun getEmail(): String? = sharedPreferences.getString("email", null)

    fun getHashedPassword() : String? = sharedPreferences.getString("password", null)

    fun setPassword(newPassword: String) = sharedPreferences.edit().putString("password", hashPassword(newPassword)).apply()

    fun logout() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun validatePassword(inputPassword: String) : Boolean {
        val realPassword = getHashedPassword()
        return realPassword == hashPassword(inputPassword)
    }

    fun validateLogin(inputUsername: String, inputPassword: String) : Boolean {
        loadUser(inputUsername)
        val realUsername = getUsername()
        return realUsername == inputUsername && validatePassword(inputPassword)
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
}