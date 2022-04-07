package com.example.kvest2.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class QuestUserRelated (
    @Embedded
    var questUser: QuestUser,

    @Relation(parentColumn = "user_id", entityColumn = "id")
    var user: User,

    @Relation(parentColumn = "quest_id", entityColumn = "id")
    var quest: Quest,
)
