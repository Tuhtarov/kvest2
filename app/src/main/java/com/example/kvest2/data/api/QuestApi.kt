package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest

data class QuestApi (
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,

    var tasks: MutableList<TaskApi>
) {
    companion object {
        fun getQuest(questApi: QuestApi): Quest {
            return Quest (
                id = questApi.id,
                name = questApi.name,
                description = questApi.description,
                createdAt = questApi.createdAt
            )
        }
    }
}