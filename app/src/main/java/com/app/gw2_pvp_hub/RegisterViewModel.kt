package com.app.gw2_pvp_hub

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

    var isLoading = MutableLiveData(false)
    var loginSuccessful = MutableLiveData(false)

    private var _errorMsg = MutableLiveData("")
    val errorMsg: LiveData<String> get() = _errorMsg

    private val TAG: String = "RegisterViewModel"

    fun registerAsync(username: String, password: String) {
        isLoading.postValue(true)

        MyApplication().app.emailPassword.registerUserAsync(username, password) {
            if (it.isSuccess) {
                loginAsync(username, password)
            } else {
                _errorMsg.postValue("Failed to register: ${it.error.errorMessage}")
                Log.e(TAG, "registerAsync: failed to register ${it.error.errorMessage}")
                isLoading.postValue(false)
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
                loginSuccessful.postValue(true)
            } else {
                Log.e(TAG, "registerAsync: Failed to log in ${it.error.errorMessage}")
            }
            isLoading.postValue(false)
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }

}
