package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskDao
import com.example.kvest2.data.entity.Task

class TaskRepository(private val dao: TaskDao) {
    suspend fun insertTasks(tasks: List<Task>): Array<Long> {
        return dao.insertTasks(tasks)
    }
}