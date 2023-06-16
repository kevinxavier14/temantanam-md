package com.capstone.temantanam.response

import com.google.gson.annotations.SerializedName

data class DeleteCollectionResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
