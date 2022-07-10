package com.app.gw2_pvp_hub.data

data class SeasonName(
    val active: Boolean,
    val divisions: List<Division>,
    val end: String,
    val id: String,
    val leaderboards: Leaderboards,
    val name: String,
    val ranks: List<Rank>,
    val start: String
)