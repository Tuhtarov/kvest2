package com.example.kvest2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Task.TABLE_NAME)
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val questId: Int? = null,
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