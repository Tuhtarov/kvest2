package com.example.kvest2.data.model

import com.example.kvest2.data.entity.User

object AppUserSingleton {
    private var user: User? = null

    public fun userNotNull(): Boolean = user != null

    public fun getUser(): User? = user

    public fun setUser(u: User) {
        if (user != null) {
            user = u
        }
    }
}