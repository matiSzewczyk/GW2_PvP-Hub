package com.app.gw2_pvp_hub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.gw2_pvp_hub.databinding.FragmentRegisterBinding

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
                val action = LoginFragmentDirections.actionGlobalTestFragment()
                findNavController().navigate(action)
            }
        }
        viewModel.loginSuccessful.observe(viewLifecycleOwner, loginObserver)

        val errorObserver = Observer<String> {
            if (it.isNotEmpty())
                Toast.makeText(
                    context, it.toString(), Toast.LENGTH_SHORT
                ).show()
        }
        viewModel.errorMsg.observe(viewLifecycleOwner, errorObserver)

        binding!!.apply {
            loginButton.setOnClickListener {
                viewModel.registerAsync(
                    userName.text.toString(),
                    password.text.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}