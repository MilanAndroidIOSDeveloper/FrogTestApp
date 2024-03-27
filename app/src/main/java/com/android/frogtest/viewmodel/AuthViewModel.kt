package com.android.frogtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.frogtest.models.Resource
import com.android.frogtest.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {


    private val _authState = MutableLiveData<Resource<Boolean>>()
    val authState: LiveData<Resource<Boolean>> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.login(email, password)
            _authState.value = result
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.signUp(email, password)
            _authState.value = result
        }
    }
}