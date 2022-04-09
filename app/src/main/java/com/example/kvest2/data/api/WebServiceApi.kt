package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest
import retrofit2.Response
import retrofit2.http.GET

interface WebServiceApi {
    @GET("quests")
    suspend fun getQuests(): Response<Quest>
}