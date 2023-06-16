package com.capstone.temantanam.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileModel(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("image_url")
    val imageUrl: String
)