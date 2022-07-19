package com.app.gw2_pvp_hub

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }

    companion object {
        var realm: Realm? = null
        var user: User? = null
    }

    fun createRealmInstance(usr: User) {
        if (realm == null) {
            user = usr
            config = SyncConfiguration.Builder(user!!, partition).build()
            realm = Realm.getInstance(config)
        }
    }
}