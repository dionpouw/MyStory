package com.jeflette.mystory.presentation.loginFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jeflette.mystory.databinding.FragmentLoginBinding
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPassword(binding)
        observeToken()

        binding?.loginButton?.setOnClickListener {
            val email = binding?.edLoginEmail?.text.toString()
            val password = binding?.edLoginPassword?.text.toString()
            when {
                email.isEmpty() -> {
                    binding?.emailEditTextLayout?.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding?.passwordEditTextLayout?.error = "Masukkan password"
                }
                else -> {
                    viewModel.submitLogin(
                        binding?.edLoginEmail?.text.toString(),
                        binding?.edLoginPassword?.text.toString()
                    )
                }
            }
            observeLogin()
            binding?.registerButton?.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    private fun checkPassword(binding: FragmentLoginBinding?) {
        binding?.edLoginPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.loginButton.isEnabled = (s.length >= 6)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeToken() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tokenResult.collect() {
                    if (it.isNotEmpty()) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }
                }
            }
        }
    }

    private fun observeLogin() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect() { response ->
                    when (response) {
                        is Resource.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                requireContext(), "Login Success", Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        }
                        is Resource.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                requireContext(), response.error?.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

}