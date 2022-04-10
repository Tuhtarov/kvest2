package com.example.kvest2.data.api

import com.example.kvest2.data.model.AppDataOrigin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl (
//                AppDataOrigin.chooseCustom("172.20.10.8", "8001").getBaseUrl()
                AppDataOrigin.chooseCustom("10.0.0.134", "8001").getBaseUrl()
//                AppDataOrigin.chooseRemote().baseUrl
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WebServiceApi by lazy {
        retrofit.create(WebServiceApi::class.java)
    }
}