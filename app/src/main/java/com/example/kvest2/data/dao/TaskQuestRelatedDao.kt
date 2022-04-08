package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskQuestRelated
import com.example.kvest2.data.entity.TaskUser

@Dao
interface TaskQuestRelatedDao {

    @Query("SELECT * FROM ${Task.TABLE_NAME} ORDER BY id ASC")
    suspend fun readAll(): MutableList<TaskQuestRelated>
}