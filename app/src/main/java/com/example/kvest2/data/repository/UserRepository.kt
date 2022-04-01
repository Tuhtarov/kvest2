package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.entity.UserDao

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
}