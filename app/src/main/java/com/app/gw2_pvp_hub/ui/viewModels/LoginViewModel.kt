package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _loginSuccessful = MutableLiveData(false)
    val loginSuccessful: LiveData<Boolean> get() = _loginSuccessful

    private val _errorMsg = MutableSharedFlow<String>()
    val errorMsg: SharedFlow<String> get() = _errorMsg.asSharedFlow()


    fun loginAsync(username: String, password: String) {
        _isLoading.postValue(true)

        MyApplication().app.loginAsync(
            Credentials.emailPassword(
                username,
                password
            )
        ) {
            if (it.isSuccess) {
                createRealm(it.get())
                _loginSuccessful.postValue(true)
            } else {
                viewModelScope.launch {
                    _errorMsg.emit("Failed to login: ${it.error.errorMessage}")
                }
                Log.e(TAG, "loginAsync: Failed to log in ${it.error.errorMessage}")
            }
            _isLoading.postValue(false)
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }
}