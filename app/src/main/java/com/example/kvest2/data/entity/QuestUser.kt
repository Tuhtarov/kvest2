package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = QuestUser.TABLE_NAME)
data class QuestUser (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val questId: Int,
    val userId: Int,

    @ColumnInfo(name = "is_current")
    val isCurrent: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "quest_user"
    }
}

/**
 * @Documentation
 * isCurrent - текущий, выбранный квест для пользователя или нет
 */