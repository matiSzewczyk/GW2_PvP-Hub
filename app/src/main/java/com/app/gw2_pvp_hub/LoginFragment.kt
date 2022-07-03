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
import com.app.gw2_pvp_hub.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login){

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

        val loadingObserver = Observer<Boolean> {
            if (it == true) {
                Toast.makeText(
                    context,
                    "Loading",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.isLoading?.observe(viewLifecycleOwner, loadingObserver)

        val loginObserver = Observer<Boolean> {
            if (it == true) {
                val action = LoginFragmentDirections.actionGlobalTestFragment()
                findNavController().navigate(action)
            }
        }

        viewModel.loginSuccessful.observe(viewLifecycleOwner, loginObserver)

        binding!!.loginButton.setOnClickListener {
            viewModel.loginAsync(
                binding!!.loginUserName.text.toString(),
                binding!!.loginPassword.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}