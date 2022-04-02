package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.entity.UserDao
import com.example.kvest2.data.model.LoggedUser

class UserRepository(private val userDao: UserDao) {
    val readAll: LiveData<List<User>> = userDao.readAll()

    private fun addUser(user: User) {
        userDao.addUser(user)
    }

    private suspend fun registration(username: String): User {
        val user = User(0, username)
        addUser(user)
        return user
    }

    suspend fun findOrCreate(username: String): LoggedUser {
        var user = userDao.findByUsername(username).firstOrNull()

        if (user == null) {
            user = registration(username)
        }

        return LoggedUser(user = user)
    }
}