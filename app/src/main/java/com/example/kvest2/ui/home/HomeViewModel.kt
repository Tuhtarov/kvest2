package com.example.kvest2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

}