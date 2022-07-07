package com.app.gw2_pvp_hub

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApi(): LeaderboardApi {
        return Retrofit.Builder()
            .baseUrl("https://api.guildwars2.com/v2/pvp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideRepository(api: LeaderboardApi): LeaderboardRepository {
        return LeaderboardRepositoryImpl(api)
    }
}
