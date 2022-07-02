package com.app.gw2_pvp_hub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gw2_pvp_hub.R
import com.example.gw2_pvp_hub.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login){

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

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
        
        binding!!.loginButton.setOnClickListener {
            // TODO: implement login logic  
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}