package com.jeflette.mystory.data.remote.response

import com.google.gson.annotations.SerializedName
import com.jeflette.mystory.data.local.entity.Story

data class ListStoriesResponse(

    @field:SerializedName("listStory") val listStory: List<Story>? = null,

    @field:SerializedName("error") val error: Boolean? = null,

    @field:SerializedName("message") val message: String? = null
)
