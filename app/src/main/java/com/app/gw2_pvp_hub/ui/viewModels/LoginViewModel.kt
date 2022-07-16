package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.models.ApiKey
import com.app.gw2_pvp_hub.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    sealed class LoginUiState {
        object Success : LoginUiState()
        object Loading : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }

    private val _uiState = MutableSharedFlow<LoginUiState>()
    val uiState: SharedFlow<LoginUiState> get() = _uiState.asSharedFlow()

    fun loginAsync(username: String, password: String) = viewModelScope.launch {
        if (username.isEmpty() || password.isEmpty()) {
            _uiState.emit(LoginUiState.Error("Please fill all fields"))
            return@launch
        }
        _uiState.emit(LoginUiState.Loading)

        MyApplication().app.loginAsync(
            Credentials.emailPassword(
                username,
                password
            )
        ) {
            if (it.isSuccess) {
                createRealm(it.get())
                viewModelScope.launch {
                    it.get().apiKeys.createAsync("api_key1") { result ->
                        if (result.isSuccess) {
                            viewModelScope.launch {
                                preferences.saveApiKey(
                                    ApiKey(
                                        result.get().name,
                                        result.get().value
                                    )
                                )
                            }
                            Log.e(TAG, "loginAsync: ${result.get().name}")
                        } else {
                            Log.e(TAG, "loginAsync: ${result.error.errorMessage}")
                        }
                    }
                }
                viewModelScope.launch {
                    _uiState.emit(LoginUiState.Success)
                }
            } else {
                viewModelScope.launch {
                    _uiState.emit(LoginUiState.Error("Failed to login: ${it.error.errorMessage}"))

                }
                Log.e(TAG, "loginAsync: Failed to log in ${it.error.errorMessage}")
            }
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}