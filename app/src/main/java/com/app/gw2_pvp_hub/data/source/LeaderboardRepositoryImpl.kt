package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.retrofit.LeaderboardApi
import com.app.gw2_pvp_hub.data.models.Leaderboard
import com.app.gw2_pvp_hub.data.models.Leaderboards
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardApi
): LeaderboardRepository {

    override suspend fun getLeaderboard(season: String, page: String): Response<Leaderboard> {
        return withContext(IO) {
            api.getLeaderboard(
                season,
            "eu",
                page
            )
        }
    }

    override suspend fun getLeaderboardList(): Response<Leaderboards> {
        return withContext(IO) {
            api.getSeasonList()
        }
    }
}
