package com.capstone.temantanam.ui.screen.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.response.GetHistoryItem
import com.capstone.temantanam.response.GetHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel: ViewModel() {

    private val _result = MutableLiveData<List<GetHistoryItem?>>()
    val result: LiveData<List<GetHistoryItem?>> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "HistoryViewModel"
    }

    fun getHistory(id: String, token: String) {
        _isLoading.value = true

        val client = ApiConfig.temanTanamApiService(token).getHistory(id)

        client.enqueue(object : Callback<GetHistoryResponse> {
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
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

            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}