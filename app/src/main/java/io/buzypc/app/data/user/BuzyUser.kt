package io.buzypc.app.data.user

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

class BuzyUser(context: Context, private var username: String) {
    var buildNameList = ArrayList<String>()
    var buildBudgetList = ArrayList<String>()

    // Each user gets its own SharedPreferences file
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("buzy-user-secrets_$username", Context.MODE_PRIVATE)

    fun isUserRegistered(): Boolean =
        sharedPreferences.contains("username") &&
                sharedPreferences.contains("password")


    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

    fun getUsername(): String? = sharedPreferences.getString("username", null)

    fun getEmail(): String? = sharedPreferences.getString("email", null)

    fun getContactNumber(): String? = sharedPreferences.getString("contactNumber", null)

    fun setUsername(newName: String) {
        sharedPreferences.edit().putString("username", newName).apply()
    }

    fun setEmail(newEmail: String) {
        sharedPreferences.edit().putString("email", newEmail).apply()
    }

    fun setContactNumber(newContactNumber: String) {
        sharedPreferences.edit().putString("contactNumber", newContactNumber).apply()
    }

    fun setPassword(newPassword: String) {
        sharedPreferences.edit().putString("password", hashPassword(newPassword)).apply()
    }

    fun logout() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun validateLogin(inputUsername: String, inputPassword: String): Boolean {
        val realUsername = getUsername()
        val realPassword = sharedPreferences.getString("password", null)

        return if (realUsername == inputUsername && realPassword == hashPassword(inputPassword)) {
            // commit() is used to ensure that the login state is written synchronously
            sharedPreferences.edit().putBoolean("isLoggedIn", true).commit()
            true
        } else {
            false
        }
    }

    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, filename: String) {
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        sharedPreferences.edit().putString("profile_pic", file.absolutePath).apply()
    }

    fun getImageFromInternalStorage(): Bitmap? {
        val filename = sharedPreferences.getString("profile_pic", null)
        return filename?.let { BitmapFactory.decodeFile(it) }
    }

    // Overloaded saveProfile methods
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

    private fun hashPassword(password: String): String {
        // Using SHA-256 for password hashing
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

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
