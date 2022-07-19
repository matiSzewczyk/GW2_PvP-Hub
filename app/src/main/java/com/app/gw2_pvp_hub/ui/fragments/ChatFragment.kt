package com.app.gw2_pvp_hub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gw2_pvp_hub.data.models.ChatMessage
import com.app.gw2_pvp_hub.databinding.FragmentChatBinding
import com.app.gw2_pvp_hub.ui.adapters.ChatAdapter
import com.app.gw2_pvp_hub.ui.viewModels.ChatViewModel
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.coroutines.flow.collectLatest

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding

    private lateinit var chatAdapter: ChatAdapter

    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var realmChangeListener: RealmChangeListener<RealmResults<ChatMessage>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        realmChangeListener = RealmChangeListener {
            println("called: RealmListener")
            viewModel.messageReceived(it)
        }
        viewModel.chatMessages?.addChangeListener(realmChangeListener)

        binding!!.apply {
            sendMessageButton.setOnClickListener {
                viewModel.senMessage(chatInput.text.toString())
            }
        }
        
        lifecycleScope.launchWhenStarted { 
            viewModel.uiState.collectLatest { 
                when (it) {
                    is ChatViewModel.UiState.ChatState -> {
                        chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
                        binding!!.chatRecyclerView.scrollToPosition(
                            chatAdapter.itemCount - 1
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRecyclerView() = binding!!.chatRecyclerView.apply {
        chatAdapter = ChatAdapter(
            ChatViewModel.UiState.ChatState.chatList
        )
        adapter = chatAdapter
        layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}