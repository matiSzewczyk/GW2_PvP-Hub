package com.app.gw2_pvp_hub.data.models

import io.realm.RealmObject
import java.util.*

open class ChatMessage(
    var id: String = UUID.randomUUID().toString(),
    var userName: String? = null,
    var content: String? = null,
    var timestamp: String? = null
) : RealmObject()
