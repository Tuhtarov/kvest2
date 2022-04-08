package com.example.kvest2.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskUserRelated (
    @Embedded
    var taskUser: TaskUser,

    @Relation(parentColumn = "user_id", entityColumn = "id")
    var user: User,

    @Relation(parentColumn = "task_id", entityColumn = "id")
    var task: Task,
)
