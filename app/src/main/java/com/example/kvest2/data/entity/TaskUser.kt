package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
    tableName = TaskUser.TABLE_NAME,
    foreignKeys = [
        ForeignKey (
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
        ),
        ForeignKey (
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
        )
    ]
)
data class TaskUser (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "user_id", index = true)
    val userId: Int,

    @ColumnInfo(name = "task_id", index = true)
    val task_id: Int,

    @ColumnInfo(name = "is_answered", defaultValue = "0")
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