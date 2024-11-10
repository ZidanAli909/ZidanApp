package com.zidan.zidanapp.AuthFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.zidan.zidanapp.MainActivity
import com.zidan.zidanapp.R
import com.zidan.zidanapp.databinding.FragmentAuthSigninBinding

class AuthSigninFragment : Fragment() {
    private lateinit var binding: FragmentAuthSigninBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            view?.findNavController()?.popBackStack(R.id.authLoginFragment, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            buttonSignin.setOnClickListener {
                val homeIntent = Intent(requireActivity(),MainActivity::class.java)
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(homeIntent)
            }
            buttonLogin.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_authSigninFragment_to_authLoginFragment)
            }
        }
    }
}