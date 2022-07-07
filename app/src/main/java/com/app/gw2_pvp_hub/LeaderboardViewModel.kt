package com.app.gw2_pvp_hub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private val TAG: String = "LeaderboardViewModel"
    private var _leaderboard = MutableLiveData<Leaderboard>()
    val leaderboard: LiveData<Leaderboard> get() = _leaderboard

    init {
        getLeaderboard()
    }

    private fun getLeaderboard() {
        viewModelScope.launch {
            val response = repository.getLeaderboard()
            if (response.isSuccessful) {
                _leaderboard.postValue(response.body()!!)
            } else {
                Log.e(TAG, "getLeaderboard: ${response.errorBody().toString()}")
            }
        }

    }
}