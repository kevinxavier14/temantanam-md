package com.capstone.temantanam.ui.screen.description

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.model.AddCollectionModel
import com.capstone.temantanam.response.AddCollectionResponse
import com.capstone.temantanam.response.GetPlantData
import com.capstone.temantanam.response.GetPlantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantDescriptionViewModel: ViewModel() {

    private val _result = MutableLiveData<GetPlantData>()
    val result: LiveData<GetPlantData> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "PlantDescViewModel"
    }

    fun getPlantByClassification(plantClassification: String, id: String, token: String) {
        _isLoading.value = true

        val client = ApiConfig.temanTanamApiService(token).getPlantByClassification(plantClassification, id)

        client.enqueue(object : Callback<GetPlantResponse> {
            override fun onResponse(
                call: Call<GetPlantResponse>,
                response: Response<GetPlantResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _result.value = response.body()?.data

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPlantResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getPlantByName(plantName: String, token: String) {
        _isLoading.value = true

        val client = ApiConfig.temanTanamApiService(token).getPlantByName(plantName)

        client.enqueue(object : Callback<GetPlantResponse> {
            override fun onResponse(
                call: Call<GetPlantResponse>,
                response: Response<GetPlantResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _result.value = response.body()?.data

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPlantResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun addCollection(plantId: Int, userId: String, token: String) {

        val addCollectionModel = AddCollectionModel(plantId)
        val client = ApiConfig.temanTanamApiService(token).addCollection(userId, addCollectionModel)

        client.enqueue(object : Callback<AddCollectionResponse> {
            override fun onResponse(
                call: Call<AddCollectionResponse>,
                response: Response<AddCollectionResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddCollectionResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}