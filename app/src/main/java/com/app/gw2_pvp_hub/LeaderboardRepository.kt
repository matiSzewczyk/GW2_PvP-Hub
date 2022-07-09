package com.app.gw2_pvp_hub

import io.realm.RealmResults
import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(season: String): Response<Leaderboard>
//    suspend fun getSeasonList(): RealmResults<RealmSeason>?
    suspend fun getSeasonIds(): Response<SeasonId>
    suspend fun getSeasonName(id: String): Response<SeasonName>
    suspend fun setSeasonList()
}
