package com.app.gw2_pvp_hub.data.models

import java.util.*


data class Message(
    var id: String = UUID.randomUUID().toString(),
    var userName: String? = null,
    var content: String? = null,
    var timestamp: String? = null,
    var messageTime: String? = null
)
