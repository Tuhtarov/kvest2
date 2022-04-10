package com.example.kvest2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.R
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.model.LoggedUser
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    val loggedUser = MutableLiveData<LoggedUser>()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val userData = userRepository.findLoggedUser()

            if (userData.user != null) {
                AppUserSingleton.user.postValue(userData.user)
            }

            loggedUser.postValue(userData)
        }
    }

    fun loginDataChanged(login: String) {
        if (login.length < 5)
            _loginFormState.value = LoginFormState(usernameError  = R.string.invalid_name)
        else
            _loginFormState.value = LoginFormState(isDataValid = true)
    }

    private suspend fun signOut() {
        userRepository.signOutUser()
    }

    fun signIn(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            signOut()

            val user = userRepository.findOrCreate(username)

            // помечаем пользователя как авторизованного
            user.isLogged = true

            userRepository.updateUser(user)

            loggedUser.postValue(LoggedUser(user = user))
        }
    }
}