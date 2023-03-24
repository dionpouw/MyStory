package com.jeflette.mystory.presentation.registerFragment

import android.os.Bundle
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
import com.jeflette.mystory.databinding.FragmentRegisterBinding
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.registerButton?.setOnClickListener {
            viewModel.submitRegister(
                binding?.edRegisterName?.text.toString(),
                binding?.edRegisterEmail?.text.toString(),
                binding?.edRegisterPassword?.text.toString()
            )
            observeRegister()
        }
    }

    private fun observeRegister() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResult.collect() { response ->
                    when (response) {
                        is Resource.Success -> {
                            Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(), response.error?.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {
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