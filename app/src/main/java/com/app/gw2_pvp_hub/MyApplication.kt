package com.app.gw2_pvp_hub

import android.app.Application
import android.content.ComponentCallbacks
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmResults
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication : Application() {

     val app: App by lazy {
         App(
             AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                 .build()
         )
    }
    private val partition = "default"
//    lateinit var realm: Realm
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
//
//    fun wtf(): RealmResults<Seasons>? {
//        var result: RealmResults<Seasons>? = null
//        realm.executeTransaction {
//            it.where(Seasons::class.java)
//                .findAll()
//        }
//        return result
//    }
}