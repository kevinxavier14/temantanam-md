package com.capstone.temantanam.response

import com.google.gson.annotations.SerializedName

data class GetCollectionResponse(

	@field:SerializedName("data")
	val data: List<GetCollectionItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GetCollectionItem(

	@field:SerializedName("collection_id")
	val collectionId: String? = null,

	@field:SerializedName("plant_id")
	val plantId: Int? = null,

	@field:SerializedName("plant_species")
	val plantSpecies: String? = null,

	@field:SerializedName("plant_image_url")
	val plantImageUrl: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
)
