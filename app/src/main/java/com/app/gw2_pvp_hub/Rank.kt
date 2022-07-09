package com.app.gw2_pvp_hub

data class Rank(
    val description: String,
    val icon: String,
    val name: String,
    val overlay: String,
    val overlay_small: String,
    val tiers: List<TierXX>
)