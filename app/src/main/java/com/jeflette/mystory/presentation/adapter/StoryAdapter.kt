package com.jeflette.mystory.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeflette.mystory.R
import com.jeflette.mystory.data.local.entity.Story
import com.jeflette.mystory.databinding.ItemStoryBinding
import com.jeflette.mystory.presentation.homeFragment.HomeFragmentDirections
import com.jeflette.mystory.util.StoryDiffCallback
import com.jeflette.mystory.util.convertDatetime

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    var storyList = ArrayList<Story>()

    fun setData(newStoryList: ArrayList<Story>) {
        val diffUtil = StoryDiffCallback(storyList, newStoryList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        storyList.clear()
        storyList.addAll(newStoryList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
        StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(storyList[position])
        holder.itemView.setOnClickListener {
            val actionHomeFragmentToDetailFragment =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(storyList[position])

            findNavController(it).currentDestination?.id?.let { id ->
                when (id) {
                    R.id.homeFragment -> findNavController(it).navigate(
                        actionHomeFragmentToDetailFragment
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = storyList.size

    inner class StoryViewHolder(private val itemBinding: ItemStoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(story: Story) {
            itemBinding.apply {
                Glide.with(itemView.context).load(story.photoUrl).into(ivItemPhoto)
                tvItemName.text = story.name
                tvItemDesc.text = story.description
                tvItemDate.text = convertDatetime(story.createdAt.toString())
            }
        }
    }
}