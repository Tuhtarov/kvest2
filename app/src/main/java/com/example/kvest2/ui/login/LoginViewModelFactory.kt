package com.example.kvest2.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.repository.UserRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val userDao = QuestDatabase.getDatabase(context = context).userDao()
            return LoginViewModel (
                userRepository = UserRepository(userDao)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}