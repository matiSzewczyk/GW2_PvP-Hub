package com.app.gw2_pvp_hub.data

import io.realm.RealmDictionary
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import java.util.*

@RealmClass
open class RealmSeason : RealmObject() {

    @PrimaryKey
    @RealmField("_id")
    var id: String? = null

    var name: String? = null
}
