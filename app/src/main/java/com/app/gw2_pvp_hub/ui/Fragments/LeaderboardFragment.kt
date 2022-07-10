package com.app.gw2_pvp_hub.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.gw2_pvp_hub.data.Leaderboard
import com.app.gw2_pvp_hub.ui.adapters.LeaderboardAdapter
import com.app.gw2_pvp_hub.R
import com.app.gw2_pvp_hub.data.Season
import com.app.gw2_pvp_hub.databinding.FragmentLeaderboardBinding
import com.app.gw2_pvp_hub.ui.ViewModels.LeaderboardViewModel

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard),
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
        _binding = FragmentLeaderboardBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        val seasonListObserver = Observer<MutableList<Season>> {
            if (it.isNotEmpty()) {
                setupSpinner()
            }
        }
        viewModel.seasonNameList.observe(viewLifecycleOwner, seasonListObserver)

        val leaderboardObserver = Observer<Leaderboard> {
            leaderboardAdapter.submitList(it)
            leaderboardAdapter.notifyDataSetChanged()
        }
        viewModel.leaderboard.observe(viewLifecycleOwner, leaderboardObserver)
    }

    private fun setupSpinner() {
        seasonSpinner = binding!!.seasonSpinner
        val seasonAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            viewModel.spinnerList
        )
        seasonSpinner.adapter = seasonAdapter
        seasonSpinner.onItemSelectedListener = this
    }

    private fun setupRecyclerView() = binding!!.leaderboardRecyclerView.apply {
        leaderboardAdapter = LeaderboardAdapter(
            viewModel.leaderboard.value!!
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