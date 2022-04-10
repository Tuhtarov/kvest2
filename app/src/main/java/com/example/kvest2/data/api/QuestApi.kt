package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.Task

data class QuestApi (
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,

    var tasks: MutableList<Task>
) {
    companion object {
        fun getQuest(questApi: QuestApi): Quest {
            return Quest (
                id = 0,
                name = questApi.name,
                description = questApi.description,
                createdAt = questApi.createdAt
            )
        }
    }
}