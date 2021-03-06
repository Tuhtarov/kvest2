package com.example.kvest2.data.api

import retrofit2.Response
import retrofit2.http.GET

interface WebServiceApi {
    @GET("quests/")
    suspend fun getQuests(): Response<MutableList<QuestApi>>
}