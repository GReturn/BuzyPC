package io.buzypc.app.Dependencies.Environment

import io.github.cdimascio.dotenv.dotenv

// From: https://github.com/cdimascio/dotenv-kotlin?tab=readme-ov-file
object Configuration {
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of '.env', use 'env'
        ignoreIfMissing = false  // Or true if you want to allow missing files
    }

    fun get(key: String): String =
        dotenv[key] ?: throw IllegalArgumentException("Missing environment variable: $key")
}