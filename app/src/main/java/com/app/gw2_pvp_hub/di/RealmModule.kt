package com.app.gw2_pvp_hub.di

import com.app.gw2_pvp_hub.data.source.ChatRepository
import com.app.gw2_pvp_hub.data.source.ChatRepositoryImpl
import com.app.gw2_pvp_hub.data.source.ProfileRepository
import com.app.gw2_pvp_hub.data.source.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RealmModule {

    @Singleton
    @Provides
    fun provideChatRepository(): ChatRepository {
        return ChatRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepositoryImpl()
    }
}