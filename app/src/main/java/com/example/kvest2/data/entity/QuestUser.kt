package com.example.kvest2.data.entity

import androidx.room.*

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
    var id: Int,

    @ColumnInfo(name = "quest_id", index = true)
    var questId: Int,

    @ColumnInfo(name = "user_id", index = true)
    var userId: Int,

    @ColumnInfo(name = "is_current", defaultValue = "0")
    var isCurrent: Boolean = false,

    @Ignore
    var quest: Quest? = null
) {
    constructor() : this(0, 0, 0, false, null)

    companion object {
        const val TABLE_NAME = "quest_user"
    }
}

/**
 * @Documentation
 * isCurrent - текущий, выбранный квест для пользователя или нет
 */