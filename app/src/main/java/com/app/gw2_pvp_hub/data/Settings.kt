package com.app.gw2_pvp_hub.data

data class Settings(
    val duration: Any,
    val name: String,
    val scoring: String,
    val tiers: List<TierX>
)