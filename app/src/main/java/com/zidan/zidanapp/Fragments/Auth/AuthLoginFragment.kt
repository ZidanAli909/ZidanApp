package com.zidan.zidanapp.Fragments.Auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.zidan.zidanapp.Activities.MainActivity
import com.zidan.zidanapp.R
import com.zidan.zidanapp.ViewModel.LoginViewModel
import com.zidan.zidanapp.databinding.FragmentAuthLoginBinding
import kotlinx.coroutines.launch

class AuthLoginFragment : Fragment() {
    private lateinit var binding: FragmentAuthLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            if (viewModel.getLoginStatus() == true) {
                val homeIntent = Intent(activity, MainActivity::class.java)
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(homeIntent)
                activity?.finish()
            }
        }
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
            lifecycleScope.launch {
                if (!viewModel.getRememberedUsername().isNullOrEmpty() && !viewModel.getRememberedPassword().isNullOrEmpty()) {
                    editTextEmail.setText(viewModel.getRememberedUsername())
                    editTextPassword.setText(viewModel.getRememberedPassword())
                    checkBoxRememberMe.isChecked = true
                }
            }

            buttonLogin.setOnClickListener {
                val username = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if (username == "Admin" && password == "12345Admin") {
                    lifecycleScope.launch {
                        viewModel.setLoginStatus(true)
                        viewModel.setRememberedUsername(
                            if (checkBoxRememberMe.isChecked) username else ""
                        )
                        viewModel.setRememberedPassword(
                            if (checkBoxRememberMe.isChecked) password else ""
                        )

                        val homeIntent = Intent(activity, MainActivity::class.java)
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(homeIntent)
                        activity?.finish()
                    }
                } else {
                    Toast.makeText(activity, "Login Gagal! Admin - 12345Admin", Toast.LENGTH_SHORT).show()
                }
            }
            buttonSignin.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_authLoginFragment_to_authSigninFragment)
            }
        }
    }
}