package com.app.gw2_pvp_hub.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    private val TAG = "MainViewModel"

    private val _login = MutableSharedFlow<UiState>()
    val login: SharedFlow<UiState> get() = _login

    sealed class UiState {
        object Success: UiState()
        object Error: UiState()
    }

    init {
        checkLoggedIn()
    }

    private fun checkLoggedIn() = viewModelScope.launch {
        MyApplication().app.loginAsync(
            Credentials.apiKey(
                getApiKey()
            )
        ) {
            if (it.isSuccess) {
                createRealm(it.get())
                viewModelScope.launch {
                    delay(1000)
                    _login.emit(UiState.Success)
                }
            } else {
                Log.e(TAG, "checkLoggedIn: ${it.error.errorMessage}")
                viewModelScope.launch {
                    delay(1000)
                    _login.emit(UiState.Error)
                }
            }
        }
    }

    private suspend fun getApiKey(): String {
        return preferences.getApiKey().toString()
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}