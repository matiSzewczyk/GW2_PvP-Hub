package com.app.gw2_pvp_hub.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.models.ChatMessage
import com.app.gw2_pvp_hub.data.models.Message
import com.app.gw2_pvp_hub.data.source.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val TAG: String = "ChatViewModel"

    sealed class UiState {
        data class Error(val errorMessage: String) : UiState()
        object ChatState : UiState() {
            var chatList: MutableList<Message> = mutableListOf()
        }
        object Empty : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    var chatMessages: RealmResults<ChatMessage> = MyApplication.realm!!.where(ChatMessage::class.java).findAll()

    init {
        getMessages()
    }

    private fun getMessages() = viewModelScope.launch {

        try {
            MyApplication.realm!!.where(ChatMessage::class.java)
                .findAll()
                .sort("timestamp", Sort.ASCENDING).forEach {
                    UiState.ChatState.chatList.add(
                        Message(
                            it.id,
                            it.userName,
                            it.content,
                            it.timestamp,
                            it.messageTime
                        )
                    )
                }
            _uiState.tryEmit(UiState.ChatState)
        } catch (e: Exception) {
            Log.e(TAG, "getMessages: ${e.message}")
            _uiState.emit(UiState.Error("Error: ${e.message}"))
        }
    }

    fun messageReceived(message: RealmResults<ChatMessage>) {
        viewModelScope.launch {
            if (message.last()!!.id != UiState.ChatState.chatList.last().id) {
                UiState.ChatState.chatList.add(
                    Message(
                        message.last()!!.id,
                        message.last()!!.userName,
                        message.last()!!.content,
                        message.last()!!.timestamp,
                        message.last()!!.messageTime
                    )
                )
                _uiState.emit(UiState.ChatState)
            } else {
                UiState.ChatState.chatList.last().content = message.last()!!.content
                _uiState.emit(UiState.ChatState)
            }
        }
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        repository.sendToRealm(message)
    }
}