package com.example.kvest2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (
    tableName = Answer.TABLE_NAME,
)
data class Answer (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String,
) {
    companion object {
        const val TABLE_NAME = "answer"
    }
}