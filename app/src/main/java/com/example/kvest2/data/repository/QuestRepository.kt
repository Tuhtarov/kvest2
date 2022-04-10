package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestDao
import com.example.kvest2.data.dao.QuestUserRelatedDao
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUserRelated

class QuestRepository(private val questDao: QuestDao, private val questRDao: QuestUserRelatedDao) {
    fun readAll(): LiveData<List<Quest>> {
        return questDao.readAll()
    }

    suspend fun findAvailableByUserId(id: Int): MutableList<Quest> {
        return questDao.findAvailableByUserId(id)
    }

    suspend fun insertQuests(quests: List<Quest>): Array<Long> {
        return questDao.insertQuests(quests)
    }

    suspend fun findCurrentQuestByUserId(id: Int): QuestUserRelated? {
        return questRDao.findCurrentQuestByUserId(id)
    }
}