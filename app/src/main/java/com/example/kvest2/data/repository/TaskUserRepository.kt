package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskUserRelatedDao
import com.example.kvest2.data.entity.TaskUserRelated

class TaskUserRepository (
    private val taskUserRelatedDao: TaskUserRelatedDao
) {
    suspend fun readAllByUserId(id: Int): MutableList<TaskUserRelated> {
        return taskUserRelatedDao.readAllByUserId(id)
    }
}