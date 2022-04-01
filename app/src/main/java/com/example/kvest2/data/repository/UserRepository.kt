package com.example.kvest2.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.kvest2.R
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.entity.UserDao
import com.example.kvest2.data.model.LoggedUser

class UserRepository(private val userDao: UserDao) {
    val readAll: LiveData<List<User>> = userDao.readAll()

    private fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun registration(login: String, pwd: String, name: String, phone: String): User {
        val user = User(0, login, pwd, name, phone)
        addUser(user)
        return user
    }

    suspend fun login(login: String, pwd: String): LoggedUser {
        val user = userDao.findByLogin(login).firstOrNull()

        return if (user?.password?.lowercase() == pwd.lowercase())
            LoggedUser(user = user)
        else
            LoggedUser(authError = R.string.invalid_password_or_username)
    }
}