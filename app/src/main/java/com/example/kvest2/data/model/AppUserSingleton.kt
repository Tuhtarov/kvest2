package com.example.kvest2.data.model

import com.example.kvest2.data.entity.User

object AppUserSingleton {
    private var user: User? = null

    fun getUser(): User? {
        return user
    }

    fun setUser(u: User) {
        user = u
    }

    fun clearUser() {
        user = null
    }
}