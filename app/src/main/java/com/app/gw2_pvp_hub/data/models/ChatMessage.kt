package com.app.gw2_pvp_hub.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import java.util.*

open class ChatMessage(
    var userName: String? = null,
    var content: String? = null,
    var timestamp: String? = null,
    var messageTime: String? = null
) : RealmObject() {
    @PrimaryKey
    @RealmField("_id")
    var id: String = UUID.randomUUID().toString()
}
