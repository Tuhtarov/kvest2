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

    suspend fun readAllRelated(): MutableList<QuestUserRelated> {
        return questUserRelatedDao.findAll()
    }

    suspend fun findAllRelatedByUserId(id: Int): MutableList<QuestUserRelated> {
        return questUserRelatedDao.findAllByUserId(id)
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