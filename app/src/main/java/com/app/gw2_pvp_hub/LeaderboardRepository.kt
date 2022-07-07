package com.app.gw2_pvp_hub

import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Response<Leaderboard>
}
