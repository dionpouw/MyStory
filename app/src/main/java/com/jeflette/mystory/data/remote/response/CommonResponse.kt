package com.jeflette.mystory.data.remote.response

import com.google.gson.annotations.SerializedName

// Untuk register response
data class CommonResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
