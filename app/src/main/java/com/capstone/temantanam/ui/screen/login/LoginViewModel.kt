package com.capstone.temantanam.ui.screen.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.temantanam.api.ApiConfig
import com.capstone.temantanam.model.LoginModel
import com.capstone.temantanam.response.LoginData
import com.capstone.temantanam.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _result = MutableLiveData<LoginData>()
    val result: LiveData<LoginData> = _result

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun authenticateUser(email: String, password: String) {
        _isLoading.value = true

        val loginModel = LoginModel(email, password)
        val client = ApiConfig.authenticationApiService().loginUser(loginModel)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    Log.e(TAG, "isSuccessful: ${response.message()}")
                    Log.e(TAG, "${response.body()}")

                    _result.value = response.body()?.data
                    _isLoggedIn.value = true // Set isLoggedIn to true on successful login
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isLoggedIn.value = false // Set isLoggedIn to false on login failure
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false

                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _isLoggedIn.value = false // Set isLoggedIn to false on network failure
            }
        })
    }
}