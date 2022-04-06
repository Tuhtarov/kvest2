package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestUserDao
import com.example.kvest2.data.entity.QuestUser

class QuestUserRepository(private val questUserDao: QuestUserDao) {
    fun readAll(): LiveData<List<QuestUser>> {
        return questUserDao.readAll()
    }

    fun addQuestUser(questUser: QuestUser) {
        questUserDao.addQuestUser(questUser)
    }

    fun clearCurrentQuestsByUserId(id: Int) {
        questUserDao.clearCurrentQuestsByUser(id)
    }

    fun setCurrentQuest(questUser: QuestUser) {
        questUserDao.setCurrentQuest (
            userId = questUser.userId,
            questUserId = questUser.id
        )
    }
}