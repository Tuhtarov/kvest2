package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskUser
import com.example.kvest2.data.entity.TaskUserRelated

@Dao
interface TaskUserRelatedDao {

    @Query("SELECT * FROM ${TaskUser.TABLE_NAME} WHERE `user_id` = :userId")
    suspend fun readAllByUserId(userId: Int): MutableList<TaskUserRelated>

    @Query("SELECT tu.* FROM ${TaskUser.TABLE_NAME} tu " +
            "JOIN ${Task.TABLE_NAME} t ON t.id = tu.task_id " +
            "WHERE tu.user_id = :userId AND t.quest_id = :questId")
    suspend fun readAllByUserIdAndQuestId(userId: Int, questId: Int): MutableList<TaskUserRelated>

    @Query("SELECT tu.* FROM ${TaskUser.TABLE_NAME} tu " +
            "JOIN ${Task.TABLE_NAME} t ON t.id = tu.task_id " +
            "WHERE tu.user_id = :userId AND tu.is_answered = 1 AND t.quest_id = :questId")
    suspend fun readCurrentAnsweredByUserIdAndQuestId(userId: Int, questId: Int): MutableList<TaskUserRelated>
}