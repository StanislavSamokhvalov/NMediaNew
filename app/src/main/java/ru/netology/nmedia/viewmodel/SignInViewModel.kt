package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.AuthRepository


class SignInViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _data = MutableLiveData<User>()
    val data: LiveData<User>
        get() = _data

    private val _dataState = MutableLiveData<FeedModelState>() // в ошибку
    val dataState: LiveData<FeedModelState>
        get() = _dataState


    fun attemptLogin(login: String, password: String) {
        viewModelScope.launch {
            try {
                val user = repository.authUser(login, password)
                _data.value = user
            } catch (e: Exception) {
                _dataState.postValue(FeedModelState(errorLogin = true))
            }
        }
    }
}
