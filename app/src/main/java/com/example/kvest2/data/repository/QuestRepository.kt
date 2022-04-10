package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.api.QuestApi
import com.example.kvest2.data.dao.QuestDao
import com.example.kvest2.data.entity.Quest

class QuestRepository(private val questDao: QuestDao) {
    fun readAll(): LiveData<List<Quest>> {
        return questDao.readAll()
    }

    suspend fun saveNonExisting(quests: List<Quest>): Int {
        var count = 0

        quests.forEach { quest ->
            if (!alreadyExist(quest)) {
                insertQuest(quest)
                count++
            }
        }

        return count
    }

    fun getQuestsFromQuestsListApi(questsApi: MutableList<QuestApi>): MutableList<Quest> {
        val fetchedQuests = mutableListOf<Quest>()

        questsApi.forEach { quest ->
            fetchedQuests.add(QuestApi.getQuest(quest))
        }

        return fetchedQuests
    }

    suspend fun findAvailableByUserId(id: Int): MutableList<Quest> {
        return questDao.findAvailableByUserId(id)
    }

    suspend fun alreadyExist(quest: Quest): Boolean {
        val quests = questDao.findQuests (
            name = quest.name,
            description = quest.description,
            createdAt = quest.createdAt
        )

        return quests.size > 0
    }

    fun insertQuest(quest: Quest) {
        questDao.insertQuest(quest)
    }

    fun findById(questId: Int): Quest? {
        return questDao.findById(questId)
    }
}