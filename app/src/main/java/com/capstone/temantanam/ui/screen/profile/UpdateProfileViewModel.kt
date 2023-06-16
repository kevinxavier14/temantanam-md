package com.capstone.temantanam.ui.screen.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.model.UpdateProfileModel
import com.capstone.temantanam.response.UpdateProfileData
import com.capstone.temantanam.response.UpdateProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileViewModel: ViewModel() {

    private val _result = MutableLiveData<UpdateProfileData>()
    val result: LiveData<UpdateProfileData> = _result

    private val _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> = _isUpdated

    companion object {
        private const val TAG = "UpdateProfileViewModel"
    }

    fun updateProfile(id: String, token: String, name: String, imageUrl: String) {

        val updateProfileModel = UpdateProfileModel(name,imageUrl)
        val client = ApiConfig.temanTanamApiService(token).updateProfile(id, updateProfileModel)

        client.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _result.value = response.body()?.data
                    _isUpdated.value = true

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    _isUpdated.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                _isUpdated.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}