package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.TaskUserRelatedDao
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.TaskUserRelated
import com.example.kvest2.data.entity.User
import javax.inject.Inject

class TaskUserRepository @Inject constructor (
    private val taskUserRelatedDao: TaskUserRelatedDao
) {
    suspend fun readAllByUserId(id: Int): MutableList<TaskUserRelated> {
        return taskUserRelatedDao.readAllByUserId(id)
    }

    suspend fun readCurrentAnsweredByUserAndQuest(user: User, quest: Quest): MutableList<TaskUserRelated> {
        return taskUserRelatedDao.readCurrentAnsweredByUserIdAndQuestId(user.id, quest.id)
    }

    suspend fun readCurrentByUserAndQuest(user: User, quest: Quest): MutableList<TaskUserRelated> {
        return taskUserRelatedDao.readAllByUserIdAndQuestId(user.id, quest.id)
    }
}