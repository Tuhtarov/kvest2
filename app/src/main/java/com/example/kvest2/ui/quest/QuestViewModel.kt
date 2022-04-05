package com.example.kvest2.ui.quest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository

class QuestViewModel (
    private val questRepository: QuestRepository,
    private val questUserRepository: QuestUserRepository
) : ViewModel() {
    private val _quests = mutableListOf(Quest(0, "sd", "df", "fds"))

    val quests = MutableLiveData<MutableList<Quest>>()

    init {
        quests.value = _quests
    }

    fun add(quest: Quest) {
        _quests.add(quest)
        quests.value = _quests
    }

    fun setChosenQuest(quest: Quest, user: User) {
        return;

        // TODO
        val questUser = QuestUser(0, quest.id, user.id, true)
        questUserRepository.clearCurrentQuestsByUser(user)
        questUserRepository.addQuestUser(questUser)
    }
}