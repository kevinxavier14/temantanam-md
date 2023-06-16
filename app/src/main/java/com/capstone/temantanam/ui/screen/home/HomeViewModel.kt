package com.capstone.temantanam.ui.screen.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.response.DeleteCollectionResponse
import com.capstone.temantanam.response.GetCollectionItem
import com.capstone.temantanam.response.GetCollectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _result = MutableLiveData<List<GetCollectionItem?>>()
    val result: LiveData<List<GetCollectionItem?>> = _result

    private val _isDeleteSuccessful = MutableLiveData<Boolean>()
    val isDeleteSuccessful: LiveData<Boolean> = _isDeleteSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun getCollection(id: String, token: String) {
        _isLoading.value = true

        val client = ApiConfig.temanTanamApiService(token).getCollection(id)

        client.enqueue(object : Callback<GetCollectionResponse> {
            override fun onResponse(
                call: Call<GetCollectionResponse>,
                response: Response<GetCollectionResponse>
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

            override fun onFailure(call: Call<GetCollectionResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun deleteCollection(collectionId: String, token: String) {
        _isDeleteSuccessful.value = false

        val client = ApiConfig.temanTanamApiService(token).deleteCollection(collectionId)

        client.enqueue(object : Callback<DeleteCollectionResponse> {
            override fun onResponse(
                call: Call<DeleteCollectionResponse>,
                response: Response<DeleteCollectionResponse>
            ) {
                if (response.isSuccessful) {
                    _isDeleteSuccessful.value = true

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    _isDeleteSuccessful.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteCollectionResponse>, t: Throwable) {
                _isDeleteSuccessful.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}