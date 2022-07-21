package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.models.ChatMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {
    override suspend fun sendToRealm(message: String) {
        MyApplication.realm!!.executeTransactionAsync {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss")

            val chatMessage = ChatMessage(
                MyApplication.user!!.profile.email.toString(),
                message,
                LocalDateTime.now().format(formatter), // Timestamp for precise comparisons.
                LocalDateTime.now().format(formatter2) // Timestamp for the ui.
            )
            it.copyToRealmOrUpdate(chatMessage)
        }
    }
}