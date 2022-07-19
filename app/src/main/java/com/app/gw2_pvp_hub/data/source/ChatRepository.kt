package com.app.gw2_pvp_hub.data.source

interface ChatRepository {
    suspend fun sendToRealm(message: String)
}