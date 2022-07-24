package com.app.gw2_pvp_hub.ui.viewModels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.source.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    sealed class UiState {
        data class ProfileImage(val bitmap: Bitmap) : UiState()
        object Empty : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    init {
        getProfilePicture()
    }

    private fun getProfilePicture() {
        viewModelScope.launch {
            val base64 = repository.getProfilePicture(MyApplication.user!!)
            if (base64 != "1") {
                val byteArray = Base64.decode(base64, Base64.DEFAULT)
                val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                _uiState.emit(UiState.ProfileImage(image))
            }
        }
    }

    fun changePicture(bitmap: Bitmap?) {
        viewModelScope.launch {
            _uiState.emit(UiState.ProfileImage(bitmap!!))
            repository.setProfilePicture(bitmap)
        }
    }
}