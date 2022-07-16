package com.app.gw2_pvp_hub.data.retrofit

import com.app.gw2_pvp_hub.data.models.Leaderboard
import com.app.gw2_pvp_hub.data.models.Leaderboards
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("seasons/{id}/leaderboards/ladder/{region}?page_size=125")
    suspend fun getLeaderboard(
        @Path("id") id: String,
        @Path("region") region: String,
        @Query("page") page: String
    ): Response<Leaderboard>

    @GET("seasons?ids=all")
    suspend fun getSeasonList(
    ): Response<Leaderboards>
}