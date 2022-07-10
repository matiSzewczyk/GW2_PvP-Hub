package com.app.gw2_pvp_hub.data.source

import android.content.ContentValues.TAG
import android.util.Log
import com.app.gw2_pvp_hub.data.retrofit.LeaderboardApi
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.data.RealmSeason
import com.app.gw2_pvp_hub.data.SeasonId
import com.app.gw2_pvp_hub.data.SeasonName
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
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

    override suspend fun getSeasonIds(): Response<SeasonId> {
        return withContext(IO) {
            api.getListofIds()
        }
    }

    override suspend fun getSeasonName(id: String): Response<SeasonName> {
        return withContext(IO) {
            api.getSeasonName(id)
        }
    }
    override suspend fun setSeasonList() {
            MyApplication.realm.executeTransactionAsync { realm ->
                try {
                    runBlocking {
                        getSeasonIds().body()!!.forEach {
                            val season = RealmSeason()
                            season.id = it
                            season.name = getSeasonName(it).body()!!.name

                            realm.copyToRealmOrUpdate(season)
                        }
                    }
                } catch (e: Throwable) {
                    Log.e(TAG, "setSeasonList: ${e.message}")
                }
            }
    }
}
