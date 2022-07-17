package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.models.ApiKey
import com.app.gw2_pvp_hub.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.App
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
        ) { user ->
            if (user.isSuccess) {
                createRealm(user.get())
                deleteApiKeys(user)
                viewModelScope.launch {
                    _uiState.emit(LoginUiState.Success)
                }
            } else {
                viewModelScope.launch {
                    _uiState.emit(LoginUiState.Error("Failed to login: ${user.error.errorMessage}"))

                }
                Log.e(TAG, "loginAsync: Failed to log in ${user.error.errorMessage}")
            }
        }
    }

    private fun deleteApiKeys(user: App.Result<User>) {
        user.get().apiKeys.fetchAll { result ->
            result.get().forEach { apiKey ->
                if (result.isSuccess) {
                    user.get().apiKeys.deleteAsync(apiKey.id) {
                        if (it.isSuccess) {
                            createApiKey(user)
                        } else {
                            Log.e(TAG, "deleteApiKeys: ${it.error.errorMessage}")
                        }
                    }
                } else {
                    Log.e(TAG, "Failure: ${result.error.errorMessage}")
                }
            }
        }
    }

    private fun createApiKey(user: App.Result<User>) {
        user.get().apiKeys.createAsync("api_key") { apiKey ->
            if (apiKey.isSuccess) {
                viewModelScope.launch {
                    preferences.saveApiKey(
                        ApiKey(
                            apiKey.get().name,
                            apiKey.get().value
                        )
                    )
                    Log.e(TAG, "createApiKey: Created ${apiKey.get().name}\t${apiKey.get().value}", )
                }
            } else {
                Log.e(TAG, "apiKeys.fetchAll(): ${apiKey.error.errorMessage}")
            }
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}