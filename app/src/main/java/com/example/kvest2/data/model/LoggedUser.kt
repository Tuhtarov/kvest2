package com.example.kvest2.data.model

import com.example.kvest2.data.entity.User

data class LoggedUser (
    val user: User? = null,
    val authError: Int? = null
)
