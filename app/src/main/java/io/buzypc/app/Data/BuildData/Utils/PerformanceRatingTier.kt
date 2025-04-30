package io.buzypc.app.Data.BuildData.Utils

enum class PerformanceRatingTier(val label: String, val description: String) {
    ENTHUSIAST("Enthusiast", "Top-tier performance for demanding tasks like AAA gaming or professional workflows"),
    POWER("Power", "Excellent performance for advanced users and multitaskers"),
    BALANCED("Balanced", "Good all-around performance for most everyday tasks"),
    ESSENTIAL("Essential", "Reliable for general productivity and light use"),
    ENTRY("Entry", "Basic setup ideal for budget-conscious users")
}