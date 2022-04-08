package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskQuestRelatedDao
import com.example.kvest2.data.entity.TaskQuestRelated

class TaskQuestRepository (
    private val taskQuestRelatedDao: TaskQuestRelatedDao
) {
    suspend fun readAll(): MutableList<TaskQuestRelated> {
        return taskQuestRelatedDao.readAll()
    }
}