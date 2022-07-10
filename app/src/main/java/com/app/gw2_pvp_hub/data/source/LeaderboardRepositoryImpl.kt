package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.retrofit.LeaderboardApi
import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.Leaderboards
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardApi
): LeaderboardRepository {

    override suspend fun getLeaderboard(season: String): Response<Leaderboard> {
        return withContext(IO) {
            api.getLeaderboard(
                season,
            "eu"
            )
        }
    }

    override suspend fun getLeaderboardList(ids: String): Response<Leaderboards> {
        return withContext(IO) {
            api.getSeasonList("all")
        }
    }
}
