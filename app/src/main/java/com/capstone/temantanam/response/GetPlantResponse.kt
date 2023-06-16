package com.capstone.temantanam.response

import com.google.gson.annotations.SerializedName

data class GetPlantResponse(

	@field:SerializedName("data")
	val data: GetPlantData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GetPlantData(

	@field:SerializedName("fertilizing_description")
	val fertilizingDescription: String? = null,

	@field:SerializedName("lightning")
	val lightning: String? = null,

	@field:SerializedName("shading")
	val shading: String? = null,

	@field:SerializedName("species")
	val species: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("watering_description")
	val wateringDescription: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("classification")
	val classification: String? = null,

	@field:SerializedName("fertilizing_recommendation")
	val fertilizingRecommendation: String? = null,

	@field:SerializedName("watering_recommendation")
	val wateringRecommendation: String? = null,
)
