package com.example.kvest2.data.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

data class TaskAnswerRelated (
    @Embedded
    var task: Task,

    @Relation(parentColumn = "correct_answer_id", entityColumn = "id")
    var correctAnswer: Answer,
)
