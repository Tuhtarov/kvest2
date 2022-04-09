package com.example.kvest2.data.model

import com.example.kvest2.data.api.RetrofitInstance
import com.example.kvest2.data.entity.TaskQuestRelated

interface ServerData {
    val host: String
    val port: String?
}

abstract class DataQuestOrigin: ServerData {
    val baseUrl = if (port == null) "http://$host" else "http://$host:$port"

    fun getQuestsWithTasks(): MutableList<TaskQuestRelated> {
        return mutableListOf()
    }
}

class CustomQuestOrigin (override val host: String, override val port: String) : DataQuestOrigin()

class RemoteQuestOrigin : DataQuestOrigin() {
    override val host = "sanctaroom.ddns.net"
    override val port = "8001"
}

object AppDataOriginSingleton {
    var questOrigin: DataQuestOrigin? = null

    var originIsRequired = false

    fun originIsAvailable() = questOrigin != null

    fun chooseRemote(): AppDataOriginSingleton {
        questOrigin = RemoteQuestOrigin()
        RetrofitInstance.init(questOrigin as RemoteQuestOrigin)

        return this
    }

    fun chooseCustom(host: String, port: String): AppDataOriginSingleton {
        questOrigin = CustomQuestOrigin(host = host, port = port)
        RetrofitInstance.init(questOrigin as CustomQuestOrigin)

        return this
    }
}