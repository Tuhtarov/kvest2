package com.example.kvest2.data.repository

import androidx.lifecycle.LiveData
import com.example.kvest2.data.dao.QuestUserDao
import com.example.kvest2.data.dao.QuestUserRelatedDao
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.QuestUserRelated

class QuestUserRepository (
    private val questUserDao: QuestUserDao,
    private val questUserRelatedDao: QuestUserRelatedDao
) {
    fun readAll(): LiveData<List<QuestUser>> {
        return questUserDao.readAll()
    }

    suspend fun findAllByUserId(id: Int): MutableList<QuestUserRelated> {
        return questUserRelatedDao.findAllByUserId(id)
    }

    suspend fun addQuestUser(questUser: QuestUser) {
        questUserDao.addQuestUser(questUser)
    }

    suspend fun clearCurrentQuestsByUserId(id: Int) {
        questUserDao.clearCurrentQuestsByUser(id)
    }

    suspend fun setCurrent(questUser: QuestUser, status: Boolean) {
        questUserDao.setCurrent (
            userId = questUser.userId,
            questUserId = questUser.id,
            status = if (status) 1 else 0
        )
    }
}