package com.app.gw2_pvp_hub

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.gw2_pvp_hub.databinding.ActivityMainBinding
import com.app.gw2_pvp_hub.ui.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigationView.setupWithNavController(navController)

        viewModel.checkLoggedIn()
        if (savedInstanceState == null) {
            binding.bottomNavigationView.isVisible = false
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collectLatest {
                when (it) {
                    is MainViewModel.UiState.Success -> {
                        if (savedInstanceState == null) {
                            binding.bottomNavigationView.isVisible = true
                            navController.navigate(
                                NavGraphDirections.actionGlobalLeaderboardFragment()
                            )
                        }
                    }
                    is MainViewModel.UiState.Error -> {
                        if (savedInstanceState == null) {
                            binding.bottomNavigationView.isVisible = true
                            navController.navigate(
                                NavGraphDirections.actionGlobalLoginFragment()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}