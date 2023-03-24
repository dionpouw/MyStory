package com.jeflette.mystory.presentation.detailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jeflette.mystory.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            Glide.with(this@DetailFragment).load(args.story.photoUrl).into(ivDetailPhoto)
            tvDetailName.text = args.story.name
            tvDetailDescription.text = args.story.description
        }
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}