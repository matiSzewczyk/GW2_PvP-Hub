package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.models.Leaderboard
import com.app.gw2_pvp_hub.data.models.Leaderboards
import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(season: String, page: String): Response<Leaderboard>
    suspend fun getLeaderboardList(): Response<Leaderboards>
}
