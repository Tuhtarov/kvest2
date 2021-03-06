package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest
import retrofit2.Response
import javax.inject.Inject

class QuestRemoteRepository @Inject constructor() {
    suspend fun getQuests(): Response<MutableList<QuestApi>> {
        return RetrofitInstance.api.getQuests()
    }

    fun getQuestsFromQuestsListApi(questsApi: MutableList<QuestApi>): List<Quest> {
        val fetchedQuests = mutableListOf<Quest>()

        questsApi.forEach { quest ->
            fetchedQuests.add(QuestApi.getQuest(quest))
        }

        return fetchedQuests
    }
}