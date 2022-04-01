package com.example.kvest2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quest")
data class Answer (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val answerText: String,
    val taskId: String
)