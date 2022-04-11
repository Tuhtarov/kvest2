package com.example.kvest2.data.model

import androidx.lifecycle.MutableLiveData
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.TaskUserRelated

object AppCurrentTasksSingleton {
    var currentQuest = MutableLiveData<Quest>()

    var currentTasksUser = MutableLiveData<MutableList<TaskUserRelated>>()
    var currentTasks = MutableLiveData<MutableList<TaskAnswerRelated>>()
    var currentTask = MutableLiveData<TaskAnswerRelated>()

    fun tasksIsNotEmpty(): Boolean = currentTasks.value?.isNotEmpty() == true
    fun taskIsAvailable(): Boolean = currentTask.value != null
    fun getTask(): TaskAnswerRelated = currentTask.value!!
}
