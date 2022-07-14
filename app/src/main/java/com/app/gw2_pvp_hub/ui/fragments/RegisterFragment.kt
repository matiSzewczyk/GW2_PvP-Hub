package com.app.gw2_pvp_hub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.gw2_pvp_hub.databinding.FragmentRegisterBinding
import com.app.gw2_pvp_hub.ui.viewModels.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                when (it) {
                    is RegisterViewModel.RegisterUiState.Error -> {
                        binding!!.innerLayout.isVisible = true
                        binding!!.progressBar.isVisible = false
                        Toast.makeText(
                            context, it.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                    is RegisterViewModel.RegisterUiState.Success -> {
                        findNavController().navigate(
                            RegisterFragmentDirections.actionGlobalLeaderboardFragment()
                        )
                    }
                    is RegisterViewModel.RegisterUiState.Loading -> {
                        binding!!.innerLayout.isVisible = false
                        binding!!.progressBar.isVisible = true
                    }
                }
            }
        }

        binding!!.apply {
            registerButton.setOnClickListener {
                viewModel.registerAsync(
                    userName.text.toString(),
                    password.text.toString(),
                    passwordConfirm.text.toString()
                )
            }

            loginButton.setOnClickListener {
                val action =
                    RegisterFragmentDirections.actionGlobalLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}