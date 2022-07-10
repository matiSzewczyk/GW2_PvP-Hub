package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.data.LeaderboardItem
import com.app.gw2_pvp_hub.data.RealmSeason
import com.app.gw2_pvp_hub.data.Season
import com.app.gw2_pvp_hub.data.source.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {


    var spinnerList = mutableListOf<String>()

    private var _leaderboard = MutableLiveData<MutableList<LeaderboardItem>>(mutableListOf())
    val leaderboard: LiveData<MutableList<LeaderboardItem>> get() = _leaderboard

    private var _seasonNameList = MutableLiveData<MutableList<Season>>(mutableListOf())
    val seasonNameList: LiveData<MutableList<Season>> get() = _seasonNameList

    init {
        getSeasonList()
//        viewModelScope.launch {
//            repository.setSeasonList()
//        }
    }

    private fun getSeasonList() {
        MyApplication.realm.executeTransactionAsync { realm ->
            val result = realm.where(RealmSeason::class.java).findAll()
            result.forEach {
                spinnerList.add(0, it.name.toString())
                _seasonNameList.value!!.add(0, Season(
                    it.id.toString(), it.name.toString()
                )
                )
            }
            _seasonNameList.postValue(_seasonNameList.value!!)
        }
    }

    fun getLeaderboard(position: Int) {
        viewModelScope.launch {
            val response = repository.getLeaderboard(_seasonNameList.value?.get(position)!!.id.toString())
            if (response.isSuccessful) {
                _leaderboard.value?.clear()
                response.body()!!.forEach {
                    _leaderboard.value!!.add(it)
                }
                _leaderboard.postValue(_leaderboard.value)
            } else {
                Log.e(TAG, "getLeaderboard: ${response.errorBody().toString()}")
            }
        }
    }
}