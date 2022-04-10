package com.example.kvest2.data.model

import com.example.kvest2.data.entity.TaskQuestRelated

interface ServerData {
    val host: String
    val port: String?
}

abstract class DataQuestOrigin: ServerData {
    fun getBaseUrl(): String {
        return if (port == null) "http://$host" else "http://$host:$port"
    }
}

class CustomQuestOrigin (override val host: String, override val port: String) : DataQuestOrigin()

class RemoteQuestOrigin : DataQuestOrigin() {
    override val host = "sanctaroom.ddns.net"
    override val port = "8001"
}

object AppDataOrigin {
    fun chooseRemote(): DataQuestOrigin {
        return RemoteQuestOrigin()
    }

    fun chooseCustom(host: String, port: String): DataQuestOrigin {
        return CustomQuestOrigin(host = host, port = port)
    }
}