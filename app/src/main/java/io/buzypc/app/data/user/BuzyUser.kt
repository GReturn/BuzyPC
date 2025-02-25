package io.buzypc.app.data.user

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

class BuzyUser(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("buzy-user-secrets", Context.MODE_PRIVATE)

    // region Getters and Setters

    fun isUserRegistered() : Boolean =
        sharedPreferences.contains("username") &&
                sharedPreferences.contains("password")

    fun isLoggedIn() : Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

    fun getUsername() : String? = sharedPreferences.getString("username", null)

    fun getEmail() : String? = sharedPreferences.getString("email", null)

    fun getContactNumber() : String? = sharedPreferences.getString("contactNumber", null)

    fun getProfilePicture(filename: String) : Bitmap? = BitmapFactory.decodeFile(filename)

    fun setUsername(newName: String) = sharedPreferences.edit()
        .putString("username", newName).apply()

    fun setEmail(newEmail: String) = sharedPreferences.edit()
        .putString("email", newEmail).apply()

    fun setContactNumber(newContactNumber: String) = sharedPreferences.edit()
        .putString("contactNumber", newContactNumber).apply()

    fun setPassword(newPassword: String) = sharedPreferences.edit()
        .putString("password", hashPassword(newPassword)).apply()

    // endregion

    fun logout() = sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

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

    fun saveUserImageToInternalStorage(context: Context, bitmapImage: Bitmap, filename: String): String {
        val file = File(context.filesDir, filename)
        try {
            FileOutputStream(file).use {
                outputStream -> bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun saveProfile(username: String, email: String, password: String) {
        val hashedPassword = hashPassword(password)

        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .putString("password", hashedPassword)
            .apply()
    }
    fun saveProfile(username: String, email: String) {
        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .apply()
    }

    private fun hashPassword(password: String) : String {
        // Message Digest: https://www.geeksforgeeks.org/message-digest-in-information-security/
        // List of algorithms: https://developer.android.com/reference/java/security/MessageDigest
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}