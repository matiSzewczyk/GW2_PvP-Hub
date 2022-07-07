package com.app.gw2_pvp_hub

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitInstance @Inject constructor() {

     private lateinit var api: LeaderboardApi

    fun buildApi(): LeaderboardApi {
        api = Retrofit.Builder()
            .baseUrl("https://api.guildwars2.com/v2/pvp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
        return api
    }
}