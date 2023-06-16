package com.capstone.temantanam.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("data")
	val data: List<GetHistoryItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GetHistoryItem(

	@field:SerializedName("plant_id")
	val plantId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("history_id")
	val historyId: String? = null,

	@field:SerializedName("plant_species")
	val plantSpecies: String? = null,

	@field:SerializedName("plant_image_url")
	val plantImageUrl: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
)
