package com.app.gw2_pvp_hub

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApi(retrofitInstance: RetrofitInstance): LeaderboardApi {
        return retrofitInstance.buildApi()
    }
}
