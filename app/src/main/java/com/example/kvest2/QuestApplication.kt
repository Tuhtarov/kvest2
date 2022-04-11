package com.example.kvest2

import android.app.Application
import android.content.Context
import com.example.kvest2.data.db.QuestDatabase

class QuestApplication: Application() {
    companion object {
        lateinit var ctx: Context
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext

        appComponent = DaggerAppComponent
            .builder()
            .database(QuestDatabase.getDatabase(ctx))
            .build()
    }
}

/**
 * Расширяем тип контекста, добавляем доступ к appComponent из контекста
 */
val Context.appComponent: AppComponent
    get() = when (this) {
        is QuestApplication -> appComponent
        else -> this.applicationContext.appComponent
    }
