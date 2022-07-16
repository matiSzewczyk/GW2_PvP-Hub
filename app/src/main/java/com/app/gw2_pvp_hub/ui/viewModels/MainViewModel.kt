package com.app.gw2_pvp_hub.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    private val TAG = "MainViewModel"
    private var apiKey: String? = null
    private val _login = MutableSharedFlow<UiState>()
    val login: SharedFlow<UiState> get() = _login

    sealed class UiState {
        object Success: UiState()
        object Error: UiState()
    }

    fun checkLoggedIn() = viewModelScope.launch {
        if (hasApiKey()) {
            MyApplication().app.loginAsync(
                Credentials.apiKey(
                    apiKey
                )
            ) {
                if (it.isSuccess) {
                    createRealm(it.get())
                    viewModelScope.launch {
                        _login.emit(UiState.Success)
                    }
                } else {
                    Log.e(TAG, "checkLoggedIn: ${it.error.errorMessage}")
                    viewModelScope.launch {
                        _login.emit(UiState.Error)
                    }
                }
            }
        }
    }

    private suspend fun hasApiKey(): Boolean {
        apiKey = preferences.getApiKey().toString()
        return apiKey != null
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}