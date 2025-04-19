package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context

// Used in Authentication Screens (LoginActivity, RegisterActivity, LogoutPromptActivity)
class BuzyAuthenticator(context: Context) {
    private val userRegistryManager = UserRegistryManager(context)
    private val buzyUserManager = BuzyUserManager(context)
    private val buzyUserSessionManager = SessionManager(context)

    fun isUserRegistered(username: String): Boolean {
        return userRegistryManager.isUserTaken(username)
    }

    fun registerUser(username: String, email: String, password: String) {
        userRegistryManager.registerUser(username)
        buzyUserManager.registerUser(username, email, password)
    }

    fun validateLogin(username: String, password: String): Boolean {
        return buzyUserManager.validateLogin(username, password)
    }

    fun loginUser(username: String) {
        buzyUserSessionManager.createSession(username)
    }

    fun logout() {
        buzyUserManager.logout()
        buzyUserSessionManager.logout()
    }
}