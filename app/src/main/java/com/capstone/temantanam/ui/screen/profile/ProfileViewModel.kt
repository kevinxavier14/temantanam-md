package com.capstone.temantanam.ui.screen.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.response.GetUserData
import com.capstone.temantanam.response.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {

    private val _result = MutableLiveData<GetUserData>()
    val result: LiveData<GetUserData> = _result

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    fun getUser(id: String, token: String) {

        val client = ApiConfig.temanTanamApiService(token).getUser(id)

        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    _result.value = response.body()?.data

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}