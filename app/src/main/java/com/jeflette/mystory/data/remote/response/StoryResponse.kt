package com.jeflette.mystory.data.remote.response

import com.google.gson.annotations.SerializedName
import com.jeflette.mystory.data.local.entity.Story

data class StoryResponse(

    @field:SerializedName("error") val error: Boolean? = null,

    @field:SerializedName("message") val message: String? = null,

    @field:SerializedName("story") val story: Story? = null
)