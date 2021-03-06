package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.UserDao
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.model.LoggedUser
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val userDao: UserDao
) {
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

    suspend fun signOutUser() {
        userDao.signOutUsers()
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