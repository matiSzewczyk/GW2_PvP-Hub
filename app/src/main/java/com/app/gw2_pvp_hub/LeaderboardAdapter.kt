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
            playerName.text = players[position].name
        }
    }

    override fun getItemCount() = players.size

    fun submitList(data: Leaderboard?) {
        players = data!!
    }
}