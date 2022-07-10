package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.Leaderboards
import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(season: String, page: String): Response<Leaderboard>
    suspend fun getLeaderboardList(): Response<Leaderboards>
}
