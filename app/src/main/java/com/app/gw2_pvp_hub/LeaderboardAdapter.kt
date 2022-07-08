package com.app.gw2_pvp_hub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gw2_pvp_hub.databinding.LeaderboardItemBinding

class LeaderboardAdapter(
    private var players: Leaderboard
) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    inner class LeaderboardViewHolder(val binding: LeaderboardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        return LeaderboardViewHolder(
            LeaderboardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.binding.apply {
            playerRank.text = players[position].rank.toString()
            playerName.text = players[position].name
            playerRating.text = players[position].scores[0].value.toString()
            playerWins.text = players[position].scores[1].value.toString()
            playerLosses.text = players[position].scores[2].value.toString()
        }
    }

    override fun getItemCount() = players.size

    fun submitList(data: Leaderboard?) {
        players = data!!
    }
}