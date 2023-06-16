package com.capstone.temantanam.ui.screen.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.model.ClassifyPlantModel
import com.capstone.temantanam.response.ClassifyPlantResponse
import com.capstone.temantanam.response.LoginData
import com.capstone.temantanam.response.LoginResponse
import com.capstone.temantanam.ui.screen.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraViewModel: ViewModel() {

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _isClassifySuccess = MutableLiveData<Boolean>()
    val isClassifySuccess: LiveData<Boolean> = _isClassifySuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "CameraViewModel"
    }

    fun classifyPlant(capturedImageBase64: String) {
        _isLoading.value = true

        val classifyPlantModel = ClassifyPlantModel(capturedImageBase64)
        val client = ApiConfig.classifyPlantApiService().classifyPlant(classifyPlantModel)

        client.enqueue(object : Callback<ClassifyPlantResponse> {
            override fun onResponse(
                call: Call<ClassifyPlantResponse>,
                response: Response<ClassifyPlantResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _result.value = response.body()?.result
                    _isClassifySuccess.value = true // Set to true on successful response

                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")
                } else {
                    _isClassifySuccess.value = false // Set to false on response failure
                    Log.e(TAG, "onFailure: ${response.message()}")
                }            }

            override fun onFailure(call: Call<ClassifyPlantResponse>, t: Throwable) {
                _isLoading.value = false

                _isClassifySuccess.value = false // Set to false on network failure
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}