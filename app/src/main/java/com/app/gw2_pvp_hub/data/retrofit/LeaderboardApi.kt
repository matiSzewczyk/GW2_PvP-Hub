package com.app.gw2_pvp_hub.data.retrofit

import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.Leaderboards
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaderboardApi {

    @GET("seasons/{id}/leaderboards/ladder/{region}?page_size=200")
    suspend fun getLeaderboard(
        @Path("id") id: String,
        @Path("region") region: String
    ): Response<Leaderboard>

    @GET("seasons")
    suspend fun getSeasonList(
        @Query("ids") ids: String
    ): Response<Leaderboards>
}