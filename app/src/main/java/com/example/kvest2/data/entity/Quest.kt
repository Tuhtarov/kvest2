package com.example.kvest2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quest")
data class Quest (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String
)