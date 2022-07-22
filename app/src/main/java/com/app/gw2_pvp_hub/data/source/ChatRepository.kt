package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.data.models.ChatMessage

interface ChatRepository {
    suspend fun sendToRealm(message: String)

    fun sentBySameUser(
        previousMessage: ChatMessage, currentMessage: ChatMessage
    ): Boolean

    fun sentWithinTwoMinutes(
        previousMessage: ChatMessage, currentMessage: ChatMessage
    ): Boolean
}