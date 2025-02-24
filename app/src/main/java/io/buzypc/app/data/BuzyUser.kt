package io.buzypc.app.data

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

class BuzyUser(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("buzy-user-secrets", Context.MODE_PRIVATE)

    fun isUserRegistered() : Boolean {
        return sharedPreferences.contains("username") && sharedPreferences.contains("password")
    }

    fun getUsername() : String? = sharedPreferences.getString("username", null)

    fun getEmail() : String? = sharedPreferences.getString("email", null)

    fun setUsername(newName: String) = sharedPreferences.edit()
        .putString("username", newName).apply()

    fun setEmail(newEmail: String) = sharedPreferences.edit()
        .putString("email", newEmail).apply()

    fun setPassword(newPassword: String) = sharedPreferences.edit()
        .putString("password", hashPassword(newPassword)).apply()

    fun validateLogin(inputUsername: String, inputPassword: String) : Boolean {
        val realUsername = getUsername()
        val realPassword = sharedPreferences.getString("password", null)

        return realUsername == inputUsername
                && realPassword == hashPassword(inputPassword)
    }

    fun saveProfile(username: String, email: String, password: String) {
        val hashedPassword = hashPassword(password)

        sharedPreferences.edit()
            .putString("username", username)
            .putString("email", email)
            .putString("password", hashedPassword)
            .apply()
    }

    private fun hashPassword(password: String) : String {
        // Message Digest: https://www.geeksforgeeks.org/message-digest-in-information-security/
        // List of algorithms: https://developer.android.com/reference/java/security/MessageDigest
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}