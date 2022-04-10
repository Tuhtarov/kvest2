package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Task

class TaskRemoteRepository(val answerRemoteRepository: AnswerRemoteRepository) {
    fun getTasksFromQuestListApi(questsApi: List<QuestApi>): List<Task> {
        val tasks = mutableListOf<Task>()

        questsApi.forEach{ questApi ->
            questApi.tasks.forEach {
                if (it.questId != null && geoPointIsCorrect(it)) {
                    tasks.add(TaskApi.toTask(it))
                }
            }
        }

        return tasks
    }

    private fun geoPointIsCorrect(task: TaskApi): Boolean {
        val lat = task.latitude.toDoubleOrNull()
        val lon = task.longitude.toDoubleOrNull()

        return lat != null && lon != null
    }
}