package com.app.gw2_pvp_hub.data.source

import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.models.ChatMessage
import io.realm.Sort
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    private val formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss")

    override suspend fun sendToRealm(message: String) {
        MyApplication.realm!!.executeTransactionAsync {


            val currentMessage = ChatMessage(
                MyApplication.user!!.profile.email.toString(),
                message,
                LocalDateTime.now().format(formatter), // Timestamp for precise comparisons.
                LocalDateTime.now().format(formatter2) // Timestamp for the ui.
            )

            val previousMessage = it.where(ChatMessage::class.java)
                .findAll()
                .sort("timestamp", Sort.ASCENDING)
                .last(null)

            if (previousMessage != null) {
                if (sentWithinTwoMinutes(previousMessage, currentMessage) &&
                    sentBySameUser(previousMessage, currentMessage)
                ) {

                    previousMessage.content =
                        "${previousMessage.content}\n${currentMessage.content}"
                    it.copyToRealmOrUpdate(previousMessage)
                    return@executeTransactionAsync
                }
            }

            it.copyToRealmOrUpdate(currentMessage)
        }
    }

    override fun sentWithinTwoMinutes(
        previousMessage: ChatMessage, currentMessage: ChatMessage
    ): Boolean {
        val previousTimestamp = LocalDateTime.parse(currentMessage.timestamp, formatter)
        return LocalDateTime.parse(
            previousMessage.timestamp,
            formatter
        ).isAfter(previousTimestamp.minusMinutes(2))
    }

    override fun sentBySameUser(
        previousMessage: ChatMessage, currentMessage: ChatMessage
    ): Boolean {
        return previousMessage.userName == currentMessage.userName
    }

}