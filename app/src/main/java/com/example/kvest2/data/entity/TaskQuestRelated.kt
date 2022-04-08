package com.example.kvest2.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskQuestRelated (
    @Embedded
    var task: Task,

    @Relation(parentColumn = "quest_id", entityColumn = "id")
    var quest: Quest,
)
