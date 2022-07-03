package com.app.gw2_pvp_hub

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: MyApplication
): ViewModel() {
    private val TAG: String = "LoginViewModel"

    var isLoading: MutableLiveData<Boolean>? = null
    var loginSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loginAsync(username: String, password: String) {
        isLoading?.postValue(true)

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
            isLoading?.postValue(false)
        }
    }

    private fun createRealm(user: User) {
        MyApplication().user = user
        MyApplication().createRealmInstance()
    }
}