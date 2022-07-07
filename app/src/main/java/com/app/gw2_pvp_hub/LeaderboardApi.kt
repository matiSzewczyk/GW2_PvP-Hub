package com.app.gw2_pvp_hub

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LeaderboardApi {

    @GET("seasons/{id}/leaderboards/ladder/{region}?page_size=250")
    suspend fun getLeaderboard(
        @Path("id") id: String,
        @Path("region") region: String
    ): Response<Leaderboard>
}