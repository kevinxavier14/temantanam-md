package com.capstone.temantanam.api

import com.capstone.temantanam.model.*
import com.capstone.temantanam.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // authentication
    @POST("auth/register")
    fun registerUser(
        @Body registerModel: RegisterModel
    ): Call<RegisterResponse>

    @POST("auth/login")
    fun loginUser(
        @Body loginModel: LoginModel
    ): Call<LoginResponse>

    // identify plant, uses 2 API (one to Flask, one to Backend)
    @POST("classify_base64")
    fun classifyPlant(
        @Body classifyPlantModel: ClassifyPlantModel
    ): Call<ClassifyPlantResponse>

    @GET("plant/{classification}/{userId}")
    fun getPlantByClassification(
        @Path("classification") plantClassification: String,
        @Path("userId") userId: String
    ): Call<GetPlantResponse>

    @GET("plant/{plantName}")
    fun getPlantByName(
        @Path("plantName") plantName: String
    ): Call<GetPlantResponse>

    // profile
    @GET("user/{id}")
    fun getUser(
        @Path("id") id: String
    ): Call<GetUserResponse>

    @PUT("user/update/{id}")
    fun updateProfile(
        @Path("id") id: String,
        @Body updateProfileModel: UpdateProfileModel
    ): Call<UpdateProfileResponse>

    // collection
    @GET("collection/{userId}")
    fun getCollection(
        @Path("userId") id: String
    ): Call<GetCollectionResponse>

    @POST("collection/{userId}")
    fun addCollection(
        @Path("userId") id: String,
        @Body addCollectionModel: AddCollectionModel
    ): Call<AddCollectionResponse>

    @DELETE("collection/delete/{collectionId}")
    fun deleteCollection(
        @Path("collectionId") collectionId: String
    ): Call<DeleteCollectionResponse>

    // history
    @GET("history/{userId}")
    fun getHistory(
        @Path("userId") id: String
    ): Call<GetHistoryResponse>
}