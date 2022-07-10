package com.app.gw2_pvp_hub

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

@HiltAndroidApp
class MyApplication : Application() {

     val app: App by lazy {
         App(
             AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                 .build()
         )
    }
    private val partition = "default"
    private lateinit var config: SyncConfiguration
    private lateinit var user: User

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }

    companion object {
        lateinit var realm: Realm
    }

    fun createRealmInstance(usr: User) {
        user = usr
        config = SyncConfiguration.Builder(user, partition).build()
        realm = Realm.getInstance(config)
    }
}