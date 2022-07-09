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

    var seasonList = mutableListOf<String>()
    private val TAG: String = "LeaderboardViewModel"

    private var _leaderboard = MutableLiveData(Leaderboard())
    val leaderboard: LiveData<Leaderboard> get() = _leaderboard

    private var _seasonNameList = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
    val seasonNameList: LiveData<MutableMap<String, String>> get() = _seasonNameList


    init {
//        getLeaderboard()
        getSeasonList()
    }

    private fun getSeasonList() {
        viewModelScope.launch {
                _seasonNameList.value!!["yo"] = "th"
                seasonList = _seasonNameList.value!!.keys.toMutableList()
        }
    }

    fun getLeaderboard() {
        viewModelScope.launch {
            val response = repository.getLeaderboard()
            if (response.isSuccessful) {
                _leaderboard.postValue(response.body())
            } else {
                Log.e(TAG, "getLeaderboard: ${response.errorBody().toString()}")
            }
        }
    }
}