package com.example.kvest2.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kvest2.R
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _registrationFormState = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _registrationFormState

    fun registrationDataChanged(login: String, pwd: String, name: String, phone: String) {
        when {
            login.length < 5 -> {
                _registrationFormState.value = RegistrationFormState(loginError = R.string.invalid_login)
            }
            pwd.length < 5 -> {
                _registrationFormState.value = RegistrationFormState(passwordError = R.string.invalid_password)
            }
            name.length < 2 -> {
                _registrationFormState.value = RegistrationFormState(nameError = R.string.invalid_name)
            }
            phone.length < 11 -> {
                _registrationFormState.value = RegistrationFormState(phoneError = R.string.invalid_phone)
            }
            else -> {
                _registrationFormState.value = RegistrationFormState(isDataValid = true)
            }
        }
    }

    @DelicateCoroutinesApi
    fun registration(login: String, pwd: String, name: String, phone: String) {
        GlobalScope.launch(Dispatchers.Default) {
            userRepository.registration(login, pwd, name, phone)
        }
    }
}