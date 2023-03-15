package com.jeflette.mystory.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeflette.mystory.data.local.entity.Story
import com.jeflette.mystory.databinding.ItemStoryBinding

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    var storyList = ArrayList<Story>()

    fun setData(storyList: ArrayList<Story>) {
        this.storyList = storyList
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
        }
    }

    override fun getItemCount(): Int = storyList.size

    inner class StoryViewHolder(private val itemBinding: ItemStoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(story: Story) {
            itemBinding.apply {
                Glide.with(itemView.context).load(story.photoUrl).into(itemStoryImg)
                itemStoryName.text = story.name
                itemStoryDesc.text = story.description
                itemStoryDate.text = story.createdAt
            }
        }
    }
}