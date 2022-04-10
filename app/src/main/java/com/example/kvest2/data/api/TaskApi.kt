package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Answer
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.Task

data class TaskApi(
    var id: Int,
    var questId: Int? = null,
    val correctAnswerId: Int,
    val question: String,
    val latitude: String,
    val longitude: String,
    val score: Int? = 0,
    val priority: Int? = 0,

    var correctAnswer: Answer?,
) {
    companion object {
        fun getTasks(questApi: QuestApi): MutableList<Task> {
            repeat(questApi.tasks.size) {
                questApi.tasks[it].id = 0
            }

            return questApi.tasks
        }

//
//        fun getCorrectAnswer(taskApi: TaskApi): MutableList<Answer> {
//            val answers = mutableListOf<Answer>()
//
//            repeat(taskApi.answer.size) {
//                taskApi.answer[it].id = 0
//
//                answers.add(taskApi.answer[it])
//            }
//        }

        fun setTasksToQuest(tasks: MutableList<Task>, quest: Quest) {
            repeat(tasks.size) {
                tasks[it].questId = quest.id
            }
        }
    }
}
