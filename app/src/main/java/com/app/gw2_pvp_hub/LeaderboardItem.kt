package com.app.gw2_pvp_hub

data class LeaderboardItem(
    val date: String,
    val name: String,
    val rank: Int,
    val scores: List<Score>
)