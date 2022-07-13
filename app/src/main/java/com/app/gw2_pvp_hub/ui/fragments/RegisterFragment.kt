package com.app.gw2_pvp_hub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.gw2_pvp_hub.R
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

        val loadingObserver = Observer<Boolean> {
            if (it == true) {
                binding!!.apply {
                    innerLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
            } else {
                binding!!.apply {
                    innerLayout.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner, loadingObserver)

        val loginObserver = Observer<Boolean> {
            if (it == true) {
                val action =
                    RegisterFragmentDirections.actionGlobalLeaderboardFragment()
                findNavController().navigate(action)
            }
        }
        viewModel.loginSuccessful.observe(viewLifecycleOwner, loginObserver)

        lifecycleScope.launchWhenStarted {
            viewModel.errorMsg.collectLatest {
                Toast.makeText(
                    context, it, Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding!!.apply {
            registerButton.setOnClickListener {
                if (userName.text.isEmpty() || password.text.isEmpty() || passwordConfirm.text.isEmpty()) {
                    Toast.makeText(
                        context, R.string.fill_all_values, Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (password.text.toString() != passwordConfirm.text.toString()) {
                    Toast.makeText(
                        context, R.string.passwords_dont_match, Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.registerAsync(
                    userName.text.toString(),
                    password.text.toString()
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