package com.example.kvest2.ui.login

import android.util.Log
import android.widget.Toast
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
import kotlin.math.log

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser


    fun loginDataChanged(login: String, pwd: String) {
        if (login.length < 5) {
            _loginFormState.value = LoginFormState(usernameError  = R.string.invalid_login)
        } else if (pwd.length < 5) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    @DelicateCoroutinesApi
    fun login(login: String, pwd: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val loggedUser = userRepository.login(login, pwd)

            if (loggedUser.authError != null) {
                Log.e("AUTH_ERROR", loggedUser.authError.toString())
            }

            if (loggedUser.user != null) {
                Log.e("AUTH_SUCCESS", loggedUser.user.name)
                _loggedUser.postValue(loggedUser)
            }
        }
    }
}