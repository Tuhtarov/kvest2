package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestUserDao
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.User

class QuestUserRepository(private val questUserDao: QuestUserDao) {
    fun readAll(): LiveData<List<QuestUser>> {
        return questUserDao.readAll()
    }

    fun addQuestUser(questUser: QuestUser) {
        questUserDao.addQuestUser(questUser)
    }

    fun clearCurrentQuestsByUser(user: User) {
        questUserDao.clearCurrentQuestsByUser(user.id)
    }
}