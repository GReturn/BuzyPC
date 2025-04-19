package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context

// UserRegistryManager manages the list of registered users for easy lookup.
class UserRegistryManager(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("buzy-user-registry", Context.MODE_PRIVATE)

    companion object {
        // stored as a CSV string
        private const val KEY_REGISTERED_USERS = "registered_users"
    }

    // Check if a username/email is already registered
    fun isUserTaken(username: String): Boolean {
        val users = getRegisteredUsers()
        return users.contains(username)
    }

    // Register a new user
    fun registerUser(username: String) {
        val users = getRegisteredUsers().toMutableSet()
        users.add(username)
        saveRegisteredUsers(users)
    }

    // Helper: Get all registered usernames
    private fun getRegisteredUsers(): Set<String> {
        val csv = sharedPreferences.getString(KEY_REGISTERED_USERS, "") ?: ""
        return if (csv.isBlank()) emptySet() else csv.split(",").toSet()
    }

    // Helper: Save the user set back as CSV
    private fun saveRegisteredUsers(users: Set<String>) {
        val csv = users.joinToString(",")
        sharedPreferences.edit().putString(KEY_REGISTERED_USERS, csv).apply()
    }
}