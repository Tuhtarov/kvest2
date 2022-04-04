package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestDao
import com.example.kvest2.data.entity.Quest

class QuestRepository(private val questDao: QuestDao) {
    fun readAll(): LiveData<List<Quest>> {
        return questDao.readAll()
    }

    fun addQuest(quest: Quest) {
        questDao.addQuest(quest)
    }
}