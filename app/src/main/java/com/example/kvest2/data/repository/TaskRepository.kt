package com.example.kvest2.data.repository

import android.util.Log
import com.example.kvest2.data.dao.TaskAnswerRelatedDao
import com.example.kvest2.data.dao.TaskDao
import com.example.kvest2.data.dao.TaskUserDao
import com.example.kvest2.data.entity.Task
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.TaskUser
import com.example.kvest2.data.entity.User
import javax.inject.Inject

class TaskRepository @Inject constructor (
    private val dao: TaskDao,
    private val daoU: TaskUserDao,
    private val daoR: TaskAnswerRelatedDao,
) {
    suspend fun insertTasks(tasks: List<Task>): Array<Long> {
        return dao.insertTasks(tasks)
    }

    suspend fun getAllRelatedByQuestId(id: Int): MutableList<TaskAnswerRelated> {
        return daoR.readAllByQuestId(id)
    }

    suspend fun clearCurrentTasks(user: User) {
        Log.i("current-tasks", "Изменены статусы is_current = 0 у текущих задач в БД" +
                ", у пользователя ${user.name}")

        daoU.clearCurrentTasks(user.id)
    }

    suspend fun getTaskWithAnswerFromCurrentTask(task: Task): TaskAnswerRelated  {
        return daoR.findById(task.id)[0]
    }

    suspend fun updateTaskUser(taskUser: TaskUser) {
        daoU.update(taskUser)
    }

    suspend fun saveToTaskUser(task: Task, user: User, isCurrent: Boolean = false) {
        val taskUser = TaskUser (
            id = 0,
            userId = user.id,
            task_id = task.id,
            isAnswered = false,
            isCurrent = isCurrent
        )

        if (isCurrent) {
            Log.i("current-task-saved", "Задача (${task.question}) сохранена как текущая")
        }

        daoU.insert(taskUser)
    }
}