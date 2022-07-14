package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.data.LeaderboardItem
import com.app.gw2_pvp_hub.data.Season
import com.app.gw2_pvp_hub.data.source.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private var firstLaunch = true

    var selectedSpinner: Int = 0
    var spinnerList = mutableListOf<String>()

    private var _leaderboard = MutableLiveData<MutableList<LeaderboardItem>>(mutableListOf())
    val leaderboard: LiveData<MutableList<LeaderboardItem>> get() = _leaderboard

    private var _seasonNameList = MutableLiveData<MutableList<Season>>(mutableListOf())
    val seasonNameList: LiveData<MutableList<Season>> get() = _seasonNameList

    init {
        getSeasonList()
    }

    private fun getSeasonList() {
        viewModelScope.launch {
            try {
                val response = repository.getLeaderboardList()
                if (response.isSuccessful) {
                    response.body()!!.forEach {
                        _seasonNameList.value!!.add(
                            Season(
                                it.id,
                                it.name,
                                it.start
                            )
                        )
                    }
                    // Order the list based on start date
                    val orderedList = _seasonNameList.value!!.sortedByDescending {
                        it.start
                    }.toMutableList()

                    orderedList.forEach {
                        spinnerList.add(it.name.toString())
                    }

                    _seasonNameList.postValue(orderedList)
                } else {
                    Log.e(TAG, "getSeasonList: ${response.errorBody()!!.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "getSeasonList: ${e.message}")
            }
        }
    }

    fun getLeaderboard(position: Int) {
        viewModelScope.launch {
            try {
                selectedSpinner = position
                val backupList = _leaderboard.value!!
                _leaderboard.value!!.clear()
                for (i in 0..1) {
                    val response = repository.getLeaderboard(
                        _seasonNameList.value?.get(position)!!.id.toString(), i.toString()
                    )
                    if (response.isSuccessful) {
                        response.body()!!.forEach {
                            _leaderboard.value!!.add(it)
                        }
                        _leaderboard.postValue(_leaderboard.value)
                        firstLaunch = false
                    } else {
                        Log.e(TAG, "getLeaderboard: ${response.errorBody()!!.string()}")
                        _leaderboard.postValue(backupList)
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "getLeaderboard: ${e.message}")
            }
        }
    }

    fun isFirstLaunch() {
        if (firstLaunch) {
            getLeaderboard(0)
        }
    }
}