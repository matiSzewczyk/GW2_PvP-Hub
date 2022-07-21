package com.app.gw2_pvp_hub.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gw2_pvp_hub.data.models.Message
import com.app.gw2_pvp_hub.databinding.ChatItemBinding

class ChatAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(
        val binding: ChatItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding.apply {
            messageUsername.text = messages[position].userName
            messageContent.text = messages[position].content
            messageTimestamp.text = messages[position].messageTime
        }
    }

    override fun getItemCount() = messages.size


}