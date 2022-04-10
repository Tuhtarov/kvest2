package com.example.kvest2.data.api

import retrofit2.Response

class QuestRemoteRepository() {
    suspend fun getQuests(): Response<MutableList<QuestApi>> {
        return RetrofitInstance.api.getQuests()
    }
}