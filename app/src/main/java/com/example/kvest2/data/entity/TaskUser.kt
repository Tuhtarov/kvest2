package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Task.TABLE_NAME)
data class TaskUser (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "task_id")
    val task_id: Int,

    @ColumnInfo(name = "is_answered")
    val isAnswered: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "task_user"
    }
}

/**
 * @Documentation
 * isAnswered = правильно ли пользователь ответил на вопрос
 */