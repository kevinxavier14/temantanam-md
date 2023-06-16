package com.capstone.temantanam.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserSessionViewModel : ViewModel() {
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun setUserData(id: String, token: String) {
        _userId.value = id
        _token.value = token
    }

    fun clearUserData() {
        _userId.value = ""
        _token.value = ""
    }
}