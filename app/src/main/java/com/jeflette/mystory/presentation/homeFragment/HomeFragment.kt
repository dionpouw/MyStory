package com.jeflette.mystory.presentation.homeFragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeflette.mystory.databinding.FragmentHomeBinding
import com.jeflette.mystory.presentation.adapter.StoryAdapter
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    init {
        getTokenAndStories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storiesAdapter = StoryAdapter()

        binding?.apply {
            actionLogout.setOnClickListener {
                viewModel.clearLogin()
                observeToken()
            }
            fabAddStory.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddStoryFragment())
            }
            rvStory.apply {
                adapter = storiesAdapter
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
        }
        populateList(storiesAdapter)
    }

    private fun observeToken() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tokenResult.collectLatest {
                    if (it == "") {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                    }
                }
            }
        }
    }

    private fun populateList(storiesAdapter: StoryAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.storiesResult.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            binding?.apply {
                                progressBar.visibility = View.GONE
                                rvStory.visibility = View.VISIBLE
                                errorMessage.visibility = View.GONE
                            }
                            it.data?.listStory?.let { listStory ->
                                storiesAdapter.setData(
                                    listStory as ArrayList
                                )
                            }
                        }
                        is Resource.Error -> {
                            binding?.apply {
                                progressBar.visibility = View.GONE
                                rvStory.visibility = View.GONE
                                errorMessage.apply {
                                    visibility = View.VISIBLE
                                    text = it.error?.message
                                }
                            }
                            Toast.makeText(context, it.error?.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            binding?.apply {
                                progressBar.visibility = View.VISIBLE
                                rvStory.visibility = View.GONE
                                errorMessage.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getTokenAndStories() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tokenResult.collectLatest { token ->
                    viewModel.getStories(token)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}