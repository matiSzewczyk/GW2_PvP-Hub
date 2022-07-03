package com.app.gw2_pvp_hub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Inject

@HiltAndroidApp
class MyApplication @Inject constructor(): Application() {

     val app: App by lazy {
         App(
             AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                 .build()
         )
    }
    private val partition = "default"
    private lateinit var realm: Realm
    private lateinit var config: SyncConfiguration
    lateinit var user: User

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        println("happened")
    }

    fun createRealmInstance() {
        config = SyncConfiguration.Builder(user, partition).build()
        realm = Realm.getInstance(config)
    }
}