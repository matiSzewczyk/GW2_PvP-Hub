package com.app.gw2_pvp_hub.ui.viewModels

import android.graphics.Bitmap
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
//        getProfilePicture()
    }

    private fun getProfilePicture() {
        viewModelScope.launch {
            val image = repository.getProfilePicture(MyApplication.user!!)
            _uiState.emit(UiState.ProfileImage(image))
        }
    }

    fun changePicture(bitmap: Bitmap?) {
        viewModelScope.launch {
            _uiState.emit(UiState.ProfileImage(bitmap!!))
            repository.setProfilePicture(bitmap)
        }
    }
}