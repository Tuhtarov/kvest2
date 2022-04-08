package com.example.kvest2.data.model

import com.example.kvest2.data.entity.TaskUserRelated

abstract class TaskUserRelatedStore {
    private var userTasks: List<TaskUserRelated>? = null

    fun getUserTasks(): List<TaskUserRelated>? {
        return userTasks
    }

    fun setUserTasks(tasks: List<TaskUserRelated>) {
        userTasks = tasks
    }
}

object AppTaskUserSingleton : TaskUserRelatedStore() {

}