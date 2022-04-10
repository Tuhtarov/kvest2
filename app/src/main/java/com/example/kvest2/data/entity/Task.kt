package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = Task.TABLE_NAME,
    foreignKeys = [
        ForeignKey (
            entity = Quest::class,
            parentColumns = ["id"],
            childColumns = ["quest_id"],
        ),
        ForeignKey (
            entity = Answer::class,
            parentColumns = ["id"],
            childColumns = ["correct_answer_id"],
        )
    ]
)
data class Task (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "quest_id", index = true)
    var questId: Int? = null,

    @ColumnInfo(name = "correct_answer_id", index = true)
    val correctAnswerId: Int,

    val question: String,
    val latitude: String,
    val longitude: String,
    val score: Int? = 0,
    val priority: Int? = 0
) {
    companion object {
        const val TABLE_NAME = "task"
    }
}

/**
 * @Documentation
 * score - количество баллов за правильный ответ
 * correctAnswerId - ссылка на правильный ответ
 * questId - ссылка на квест, к которому принадлежит таск
 * latitude - долгога (геопоинт выполнения задания)
 * longitude - широта (геопоинт выполнения задания
 * priority - приоритет задачи, чем выше приоритет,
 *            тем первее будет стоять её выполнение в списке задач,
 *            может пригодится, если возможность выполнения какой-либо следующей задачи,
 *            будет зависеть от выполнения предыдущей задачи с более высшим приоритетом
 */