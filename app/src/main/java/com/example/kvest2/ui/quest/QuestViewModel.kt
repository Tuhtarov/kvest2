package com.example.kvest2.ui.quest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.repository.QuestRepository

class QuestViewModel (private val questRepository: QuestRepository) : ViewModel() {
    val _quests = mutableListOf(Quest(0, "sd", "df", "fds"))

    val quests = MutableLiveData<MutableList<Quest>>()

    init {
        quests.value = _quests
    }

    fun add(quest: Quest) {
        _quests.add(quest)
        quests.value = _quests
    }
}