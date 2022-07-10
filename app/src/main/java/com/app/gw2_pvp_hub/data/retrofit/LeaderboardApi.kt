package com.app.gw2_pvp_hub.data.retrofit

import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.SeasonId
import com.app.gw2_pvp_hub.data.SeasonName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LeaderboardApi {

    @GET("seasons/{id}/leaderboards/ladder/{region}?page_size=200")
    suspend fun getLeaderboard(
        @Path("id") id: String,
        @Path("region") region: String
    ): Response<Leaderboard>

    @GET("seasons")
    suspend fun getListofIds(): Response<SeasonId>

    @GET("seasons/{id}")
    suspend fun getSeasonName(
        @Path("id") id: String
    ): Response<SeasonName>
}