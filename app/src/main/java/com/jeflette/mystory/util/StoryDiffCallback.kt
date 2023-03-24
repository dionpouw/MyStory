package com.jeflette.mystory.util

import androidx.recyclerview.widget.DiffUtil
import com.jeflette.mystory.data.local.entity.Story

class StoryDiffCallback(
    private val oldList: ArrayList<Story>, private val newList: ArrayList<Story>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}