package com.app.gw2_pvp_hub

import io.realm.RealmResults
import retrofit2.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(): Response<Leaderboard>
    suspend fun getSeasonList(): RealmResults<Seasons>?
}
