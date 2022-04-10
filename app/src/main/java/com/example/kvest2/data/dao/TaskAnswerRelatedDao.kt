package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskAnswerRelated

@Dao
interface TaskAnswerRelatedDao {
    @Query("SELECT * FROM ${Task.TABLE_NAME} WHERE `quest_id` = :questId ORDER BY id ASC")
    suspend fun readAllByQuestId(questId: Int): MutableList<TaskAnswerRelated>
}