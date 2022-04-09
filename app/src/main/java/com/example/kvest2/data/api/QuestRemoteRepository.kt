package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest
import retrofit2.Response

class QuestRemoteRepository {
    suspend fun getQuests(): Response<Quest> {
        return RetrofitInstance.api!!.getQuests()
    }
}