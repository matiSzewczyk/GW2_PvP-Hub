package com.app.gw2_pvp_hub.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.data.models.ChatMessage
import com.app.gw2_pvp_hub.data.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.RealmResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    sealed class UiState {
        data class Error(val errorMessage: String) : UiState()
        object ChatState : UiState() {
            var chatList: MutableList<Message> = mutableListOf()
        }
        object Empty : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    fun messageReceived(message: RealmResults<ChatMessage>) {
        viewModelScope.launch {
            UiState.ChatState.chatList.add(
                Message(
                    message.last()!!.id,
                    message.last()!!.userName,
                    message.last()!!.content,
                    message.last()!!.timestamp
                )
            )
            _uiState.emit(UiState.ChatState)
        }
    }

}