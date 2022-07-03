package com.app.gw2_pvp_hub

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val application: MyApplication
) : ViewModel() {

    var isLoading = MutableLiveData(false)
    var loginSuccessful = MutableLiveData(false)

    private val TAG: String = "RegisterViewModel"

    fun registerAsync(username: String, password: String) {
        isLoading.postValue(true)

        application.app.emailPassword.registerUserAsync(username, password) {
            if (it.isSuccess) {
                loginAsync(username, password)
            } else {
                Log.e(TAG, "registerAsync: failed to register ${it.error.errorMessage}")
            }
        } 
    }

    private fun loginAsync(username: String, password: String) {
        application.app.loginAsync(
            Credentials.emailPassword(
                username,
                password
            )
        ) {
            if (it.isSuccess) {
                createRealm(it.get())
                loginSuccessful.postValue(true)
            } else {
                Log.e(TAG, "loginAsync: Failed to log in ${it.error.errorMessage}")
            }
            isLoading.postValue(false)
        }
    }

    private fun createRealm(user: User) {
        MyApplication().createRealmInstance(user)
    }

}
