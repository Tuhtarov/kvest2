package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Answer
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.Task
import com.google.gson.annotations.SerializedName

data class TaskApi (
    var id: Int,

    @SerializedName("quest_id")
    var questId: Int? = null,

    @SerializedName("correct_answer_id")
    val correctAnswerId: Int,

    val question: String,
    val latitude: String,
    val longitude: String,
    val score: Int? = 0,
    val priority: Int? = 0,
    var answer: Answer?
) {
    companion object {
        fun toTask(t: TaskApi): Task {
            return Task (
                id = t.id,
                correctAnswerId = t.correctAnswerId,
                latitude = t.latitude,
                longitude = t.longitude,
                priority = t.priority,
                questId = t.questId,
                question = t.question,
                score = t.score
            )
        }
    }
}
