package io.buzypc.app.Data.SharedPrefManagers.Util

import java.security.MessageDigest

fun hashPassword(password: String) : String {
    // Message Digest: https://www.geeksforgeeks.org/message-digest-in-information-security/
    // List of algorithms: https://developer.android.com/reference/java/security/MessageDigest
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}