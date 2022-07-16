package com.app.gw2_pvp_hub.data.models

data class LeaderboardsItem(
    val active: Boolean,
    val divisions: List<Division>,
    val end: String,
    val id: String,
    val leaderboards: LeaderboardsX,
    val name: String,
    val ranks: List<Rank>,
    val start: String
)