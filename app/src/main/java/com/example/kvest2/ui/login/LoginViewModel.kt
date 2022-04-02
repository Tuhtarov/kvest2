package com.example.kvest2.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kvest2.R
import com.example.kvest2.data.model.LoggedUser
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser


    fun loginDataChanged(login: String) {
        if (login.length < 5) {
            _loginFormState.value = LoginFormState(usernameError  = R.string.invalid_name)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    @DelicateCoroutinesApi
    fun signIn(username: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val loggedUser = userRepository.findOrCreate(username)

            if (loggedUser.user != null) {
                _loggedUser.postValue(loggedUser)
            }
        }
    }
}