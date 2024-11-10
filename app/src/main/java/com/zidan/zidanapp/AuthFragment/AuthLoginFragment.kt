package com.zidan.zidanapp.AuthFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.zidan.zidanapp.MainActivity
import com.zidan.zidanapp.R
import com.zidan.zidanapp.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {
    private lateinit var binding: FragmentAuthLoginBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            buttonLogin.setOnClickListener {
                val homeIntent = Intent(requireActivity(),MainActivity::class.java)
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeIntent)
            }
            buttonSignin.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_authLoginFragment_to_authSigninFragment)
            }
        }
    }
}