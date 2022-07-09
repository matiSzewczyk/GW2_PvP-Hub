package com.app.gw2_pvp_hub

import io.realm.RealmDictionary
import io.realm.RealmMap
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import java.util.*

@RealmClass
open class Seasons : RealmObject() {
    @PrimaryKey
    @RealmField("_id")
    var id: String = UUID.randomUUID().toString()

    var name: RealmDictionary<String>? = null
}
