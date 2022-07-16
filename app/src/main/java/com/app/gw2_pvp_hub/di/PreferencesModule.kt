package com.app.gw2_pvp_hub.di

import android.content.Context
import com.app.gw2_pvp_hub.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)
}
