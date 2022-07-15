package com.app.gw2_pvp_hub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gw2_pvp_hub.databinding.FragmentLeaderboardBinding
import com.app.gw2_pvp_hub.ui.adapters.LeaderboardAdapter
import com.app.gw2_pvp_hub.ui.viewModels.LeaderboardViewModel
import kotlinx.coroutines.flow.collectLatest

class LeaderboardFragment : Fragment(),
    AdapterView.OnItemSelectedListener {

    private lateinit var seasonSpinner: Spinner
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding

    private val viewModel: LeaderboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(
            layoutInflater, container, false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSpinner()

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                when (it) {
                    is LeaderboardViewModel.LeaderboardUiState.LeaderboardState -> {
                        leaderboardAdapter.notifyDataSetChanged()
                    }
                    LeaderboardViewModel.LeaderboardUiState.SpinnerListState -> {
                        setupSpinner()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.errorMsg.collectLatest {
                Toast.makeText(
                    context, it, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupSpinner() {
        seasonSpinner = binding!!.seasonSpinner
        val seasonAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            LeaderboardViewModel.LeaderboardUiState.SpinnerListState.spinnerList
        )
        seasonSpinner.adapter = seasonAdapter
        seasonSpinner.setSelection(
            LeaderboardViewModel.LeaderboardUiState.SpinnerSelectionState().selectedItem,
            false
        )
        seasonSpinner.onItemSelectedListener = this

    }

    private fun setupRecyclerView() = binding!!.leaderboardRecyclerView.apply {
        leaderboardAdapter = LeaderboardAdapter(
            LeaderboardViewModel.LeaderboardUiState.LeaderboardState.leaderboardList
        )
        adapter = leaderboardAdapter
        layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.getLeaderboard(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}