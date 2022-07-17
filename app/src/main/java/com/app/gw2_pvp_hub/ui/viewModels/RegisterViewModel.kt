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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val preferences: UserPreferences
) : ViewModel() {

    sealed class RegisterUiState {
        object Success : RegisterUiState()
        object Loading : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
    }

    private val _uiState = MutableSharedFlow<RegisterUiState>()
    val uiState: SharedFlow<RegisterUiState> get() = _uiState

    fun registerAsync(
        username: String,
        password: String,
        passwordConfirm: String
    ) = viewModelScope.launch {

        if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            _uiState.emit(RegisterUiState.Error("Please fill all the fields."))
            return@launch
        }
        if (password != passwordConfirm) {
            _uiState.emit(RegisterUiState.Error("Passwords do not match"))
            return@launch
        }
        _uiState.emit(RegisterUiState.Loading)

        MyApplication().app.emailPassword.registerUserAsync(username, password) {
            if (it.isSuccess) {
                loginAsync(username, password)
            } else {
                viewModelScope.launch {
                    _uiState.emit(
                        RegisterUiState.Error(
                            "Failed to register ${it.error.errorMessage}"
                        )
                    )
                }
            }
        }
    }

    private fun loginAsync(username: String, password: String) {
        MyApplication().app.loginAsync(
            Credentials.emailPassword(
                username,
                password
            )
        ) { user ->
            if (user.isSuccess) {
                createRealm(user.get())
                createApiKey(user)
                viewModelScope.launch {
                    _uiState.emit(RegisterUiState.Success)
                }
            } else {
                viewModelScope.launch {
                    _uiState.emit(
                        RegisterUiState.Error(
                            "Failed to login: ${user.error.errorMessage}"
                        )
                    )
                }
                Log.e(TAG, "loginAsync: Failed to log in ${user.error.errorMessage}")
            }
        }
    }

    private fun createApiKey(user: App.Result<User>) {
        user.get().apiKeys.createAsync("api_key") { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    preferences.saveApiKey(
                        ApiKey(
                            result.get().name,
                            result.get().value
                        )
                    )
                }
                Log.e(TAG, "Created key: ${result.get().name}")
            } else {
                Log.e(TAG, "Failed creating key: ${result.error.errorMessage}")
            }
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}
