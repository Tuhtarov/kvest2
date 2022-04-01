package com.example.kvest2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val password: String,
    val name: String,
    val phone: String,
)
