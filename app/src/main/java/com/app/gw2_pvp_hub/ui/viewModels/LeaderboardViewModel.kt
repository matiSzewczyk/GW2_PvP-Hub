package com.app.gw2_pvp_hub.ui.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.gw2_pvp_hub.data.LeaderboardItem
import com.app.gw2_pvp_hub.data.Season
import com.app.gw2_pvp_hub.data.source.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    sealed class LeaderboardUiState {
        data class SpinnerSelectionState(val selectedItem: Int = 0) : LeaderboardUiState()
        object LeaderboardState : LeaderboardUiState() {
            var leaderboardList: MutableList<LeaderboardItem> = mutableListOf()
        }
        object SeasonListState : LeaderboardUiState() {
            var seasonList: MutableList<Season> = mutableListOf()
        }
        object SpinnerListState : LeaderboardUiState() {
            var spinnerList: MutableList<String> = mutableListOf()
        }
        object Empty : LeaderboardUiState()
    }

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Empty)
    val uiState: StateFlow<LeaderboardUiState> get() = _uiState.asStateFlow()

    private val _errorMsg = MutableSharedFlow<String>()
    val errorMsg: SharedFlow<String> get() = _errorMsg.asSharedFlow()

    init {
        getSeasonList()
    }

    private fun getSeasonList() {
        viewModelScope.launch {
            try {
                val response = repository.getLeaderboardList()
                if (response.isSuccessful) {
                    response.body()!!.forEach {
                        LeaderboardUiState.SeasonListState.seasonList.add(Season(
                            it.id,
                            it.name,
                            it.start
                        ))
                    }
                    val orderedList = LeaderboardUiState.SeasonListState.seasonList.sortedByDescending {
                        it.start
                    }.toMutableList()

                    orderedList.forEach {
                        LeaderboardUiState.SpinnerListState.spinnerList.add(it.name.toString())
                    }
                    LeaderboardUiState.SeasonListState.seasonList = orderedList
                    _uiState.emit(LeaderboardUiState.SeasonListState)
                    _uiState.emit(LeaderboardUiState.SpinnerListState)
                } else {
                    logError(response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                logError(e.message.toString())
            }
        }
    }

    fun getLeaderboard(position: Int) {
        viewModelScope.launch {
            try {
                _uiState.emit(LeaderboardUiState.SpinnerSelectionState(position))
                val backupList = LeaderboardUiState.LeaderboardState.leaderboardList
                LeaderboardUiState.LeaderboardState.leaderboardList.clear()
                for (i in 0..1) {
                    val response = repository.getLeaderboard(
                        LeaderboardUiState.SeasonListState.seasonList[position].id.toString(),
                        i.toString()
                    )
                    if (response.isSuccessful) {
                        response.body()!!.forEach {
                            LeaderboardUiState.LeaderboardState.leaderboardList.add(it)
                        }
                        _uiState.emit(LeaderboardUiState.LeaderboardState)
                    } else {
                        logError(response.errorBody()!!.string())
                        LeaderboardUiState.LeaderboardState.leaderboardList = backupList
                        _uiState.emit(LeaderboardUiState.LeaderboardState)
                    }
                }
            } catch (e: Exception) {
                logError(e.message.toString())
            }
        }
    }

    private fun logError(error: String) = viewModelScope.launch {
        _errorMsg.emit("Failure: $error")
        Log.e(TAG, "getLeaderboard: $error")
    }
}