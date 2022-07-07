package com.app.gw2_pvp_hub

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val api: LeaderboardApi
){
    suspend fun getLeaderboard(): Response<Leaderboard> {
        return withContext(IO) {
            api.getLeaderboard(
                "8808E0FE-8729-45B6-9BA7-50DBE3167B80",
            "eu"
            )
        }
    }

//    suspend fun getLeaderboard(id: String, region: String) = api.getLeaderboard(id, region)
}