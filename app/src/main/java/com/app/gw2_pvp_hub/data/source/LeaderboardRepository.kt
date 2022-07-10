package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.SeasonId
import com.app.gw2_pvp_hub.data.SeasonName
import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(season: String): Response<Leaderboard>
    suspend fun getSeasonIds(): Response<SeasonId>
    suspend fun getSeasonName(id: String): Response<SeasonName>
    suspend fun setSeasonList()
}
