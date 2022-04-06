package com.example.kvest2.ui.quest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для фрагментов списка пользовательских квестов
 * и для добавления квеста (Quest) в пользовательские квесты (QuestUser)
 */
class QuestSharedViewModel (
    private val questRepository: QuestRepository,
    private val questUserRepository: QuestUserRepository,
    private val currentUser: User
) : ViewModel() {

    private val _quests = mutableListOf (
        Quest(1, "1 тестовый", "Описание квеста", "24.02.2022"),
        Quest(2, "2 тестовый", "Описание квеста", "24.02.2022"),
        Quest(3, "3 тестовый", "Описание квеста", "24.02.2022"),
        Quest(4, "4 тестовый", "Описание квеста", "24.02.2022"),
        Quest(5, "5 тестовый", "Описание квеста", "24.02.2022"),
    )

    private val _questsUser = mutableListOf (
        QuestUser(6, _quests[0].id, currentUser.id, true, quest = _quests[0]),
        QuestUser(7, _quests[1].id, currentUser.id, quest = _quests[1])
    )

    val quests = MutableLiveData<MutableList<Quest>>()

    val questsUser = MutableLiveData<MutableList<QuestUser>>()

    init {
        quests.value = _quests
        questsUser.value = _questsUser
    }

    /**
     * Добавить квест в таблицу квестов, выбранных текущим пользователем,
     * если такого квеста ещё не было добавлено
     */
    fun appendQuestToUserQuestsIfNotExists(quest: Quest) {
        var alreadyExists = false

        repeat(_questsUser.size) {
            alreadyExists = _questsUser[it].questId == quest.id || alreadyExists
        }

        if (!alreadyExists) {
            val questUser = QuestUser(0, quest.id, currentUser.id)
//            saveQuestUserToDB(questUser)
            _questsUser.add(questUser)
            questsUser.value = _questsUser
        }
    }

    private fun saveQuestUserToDB(questUser: QuestUser) {
        viewModelScope.launch {
            try {
                questUserRepository.addQuestUser(questUser)
            } catch (e: Exception) {
                Log.e("DB-ERROR", e.message.toString())
            }
        }
    }

    /**
     * Обозначить пользовательский квест как текущий
     */
    fun setChosenQuestUserHowCurrent(questUser: QuestUser) {
        viewModelScope.launch(Dispatchers.Default) {
            // снимаем статус "текущего" квеста, со всех пользовательских квестов
            questUserRepository.clearCurrentQuestsByUserId(questUser.userId)

            // назначаем пользовательский квест как "текущий", и сохраняем в БД
            questUser.isCurrent = true
            questUserRepository.setCurrentQuest(questUser)
        }
    }
}