package com.app.gw2_pvp_hub

import io.realm.RealmResults
import io.realm.mongodb.RealmResultTask
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardApi
): LeaderboardRepository {
    override suspend fun getLeaderboard(): Response<Leaderboard> {
        return withContext(IO) {
            api.getLeaderboard(
                "8808E0FE-8729-45B6-9BA7-50DBE3167B80",
            "eu"
            )
        }
    }

    override suspend fun getSeasonList(): RealmResults<Seasons>? {
        var result: RealmResults<Seasons>? = null
            MyApplication.realm.executeTransactionAsync {
                 result = it.where(Seasons::class.java)
                    .findAll()
            }
            return result
    }
}
