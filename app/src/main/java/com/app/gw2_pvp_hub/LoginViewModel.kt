package com.app.gw2_pvp_hub

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

    var isLoading: MutableLiveData<Boolean>? = null

    fun loginAsync(username: String, password: String) {
        isLoading?.postValue(true)
        application.app.loginAsync(
            Credentials.emailPassword(
                username,
                password
            )) {
            if (it.isSuccess) {
                createRealm(it.get())
            } else {
                println("failed :c")
                isLoading?.postValue(false)
            }
        }
    }

    private fun createRealm(user: User) {
        MyApplication().user = user
        MyApplication().createRealmInstance()
    }
}