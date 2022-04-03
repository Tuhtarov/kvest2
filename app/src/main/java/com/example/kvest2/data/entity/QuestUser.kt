package com.example.kvest2.data.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity (
    tableName = QuestUser.TABLE_NAME,
    foreignKeys = [
        ForeignKey (
            entity = Quest::class,
            parentColumns = ["id"],
            childColumns = ["quest_id"],
        ),
        ForeignKey (
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
        )
    ]
)
data class QuestUser (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "quest_id", index = true)
    val questId: Int,

    @ColumnInfo(name = "user_id", index = true)
    val userId: Int,

    @ColumnInfo(name = "is_current", defaultValue = "0")
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