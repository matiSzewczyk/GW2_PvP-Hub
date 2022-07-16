package com.app.gw2_pvp_hub

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.app.gw2_pvp_hub.ui.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        viewModel.checkLoggedIn()

        lifecycleScope.launchWhenStarted {
            viewModel.login.collectLatest {
                when (it) {
                    is MainViewModel.UiState.Success -> {
                        navController.navigate(
                            NavGraphDirections.actionGlobalLeaderboardFragment()
                        )
                    }
                    is MainViewModel.UiState.Error -> {
                        navController.navigate(
                            NavGraphDirections.actionGlobalLoginFragment()
                        )
                    }
                }
            }
        }
    }
}