package com.example.kvest2.data.repository

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

    private suspend fun registration(username: String): User {
        val user = User(0, username)
        addUser(user)
        return user
    }

    suspend fun findLoggedUser(): LoggedUser {
        val user = userDao.findLoggedUser().firstOrNull()

        if (user != null) {
            return LoggedUser(user = user)
        }

        return LoggedUser()
    }

    suspend fun signOutUser(user: User) {
        user.isLogged = false
        userDao.updateUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun findOrCreate(username: String): User {
        var user = userDao.findByUsername(username).firstOrNull()

        if (user == null) {
            user = registration(username)
        }

        return user
    }
}