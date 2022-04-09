package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.model.AppDataOriginSingleton
import com.example.kvest2.data.model.DataQuestOrigin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    var retrofit: Retrofit? = null

    var api: WebServiceApi? = null

    fun retrofitAvailable() = retrofit != null && api != null

    fun init(originData: DataQuestOrigin): RetrofitInstance {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(originData.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: WebServiceApi by lazy {
            retrofit.create(WebServiceApi::class.java)
        }

        this.api = api
        this.retrofit = retrofit

        return this
    }
}