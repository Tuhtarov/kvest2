package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestDao
import com.example.kvest2.data.entity.Quest

class QuestRepository(private val questDao: QuestDao) {
    fun readAll(): LiveData<List<Quest>> {
        return questDao.readAll()
    }

    suspend fun findAvailableByUserId(id: Int): MutableList<Quest> {
        return questDao.findAvailableByUserId(id)
    }

    fun addQuest(quest: Quest) {
        questDao.addQuest(quest)
    }

    fun findById(questId: Int): Quest? {
        return questDao.findById(questId)
    }
}