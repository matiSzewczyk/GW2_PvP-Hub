package com.app.gw2_pvp_hub

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _loginSuccessful = MutableLiveData(false)
    val loginSuccessful: LiveData<Boolean> get() = _loginSuccessful

    private val _errorMsg = MutableLiveData("")
    val errorMsg: LiveData<String> get() = _errorMsg

    init {
        _errorMsg.postValue("")
    }


    fun registerAsync(username: String, password: String) {
        _isLoading.postValue(true)

        MyApplication().app.emailPassword.registerUserAsync(username, password) {
            if (it.isSuccess) {
                loginAsync(username, password)
            } else {
                Log.e(TAG, "registerAsync: failed to register ${it.error.errorMessage}")
                _isLoading.postValue(false)
                _errorMsg.postValue("Failed to register: ${it.error.errorMessage}")
            }
        }
    }

    private fun loginAsync(username: String, password: String) {
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
                _errorMsg.postValue("Failed to login: ${it.error.errorMessage}")
                Log.e(TAG, "loginAsync: Failed to log in ${it.error.errorMessage}")
            }
            _isLoading.postValue(false)
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }

    fun clearError() {
        _errorMsg.postValue("")
    }
}
