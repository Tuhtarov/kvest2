package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskQuestRelatedDao
import com.example.kvest2.data.entity.TaskQuestRelated
import javax.inject.Inject

class TaskQuestRepository @Inject constructor (
    private val taskQuestRelatedDao: TaskQuestRelatedDao
) {
    suspend fun readAll(): MutableList<TaskQuestRelated> {
        return taskQuestRelatedDao.readAll()
    }
}