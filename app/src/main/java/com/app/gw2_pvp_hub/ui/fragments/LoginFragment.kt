package com.app.gw2_pvp_hub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.gw2_pvp_hub.databinding.FragmentLoginBinding
import com.app.gw2_pvp_hub.ui.viewModels.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    when (it) {
                        is LoginViewModel.LoginUiState.Success -> {
                            findNavController().navigate(
                                LoginFragmentDirections.actionGlobalLeaderboardFragment()
                            )
                        }
                        is LoginViewModel.LoginUiState.Loading -> {
                            binding!!.innerLayout.isVisible = false
                            binding!!.progressBar.isVisible = true
                        }
                        is LoginViewModel.LoginUiState.Error -> {
                            binding!!.innerLayout.isVisible = true
                            binding!!.progressBar.isVisible = false
                            Toast.makeText(
                                context, it.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding!!.apply {
            loginButton.setOnClickListener {
                viewModel.loginAsync(
                    userName.text.toString(),
                    password.text.toString()
                )
            }
            signupButton.setOnClickListener {
                val action =
                    LoginFragmentDirections.actionGlobalRegisterFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}