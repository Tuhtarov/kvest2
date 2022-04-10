package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskAnswerRelatedDao
import com.example.kvest2.data.dao.TaskDao
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskAnswerRelated

class TaskRepository(private val dao: TaskDao, private val daoR: TaskAnswerRelatedDao) {
    suspend fun insertTasks(tasks: List<Task>): Array<Long> {
        return dao.insertTasks(tasks)
    }

    suspend fun getAllRelatedByQuestId(id: Int): MutableList<TaskAnswerRelated> {
        return daoR.readAllByQuestId(id)
    }
}