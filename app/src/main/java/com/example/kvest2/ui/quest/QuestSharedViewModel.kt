package com.example.kvest2.ui.quest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.QuestUserRelated
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

    private lateinit var _questsAvailable: MutableList<Quest>
    private lateinit var _questUserRelated: MutableList<QuestUserRelated>

    val questsAvailable = MutableLiveData<MutableList<Quest>>()
    val questUserRelated = MutableLiveData<MutableList<QuestUserRelated>>()

    init {
        viewModelScope.launch {
            launch {
                _questsAvailable = questRepository.findAvailableByUserId(currentUser.id)
                questsAvailable.postValue(_questsAvailable)
            }

            launch {
                _questUserRelated = questUserRepository.findAllRelatedByUserId(currentUser.id)
                questUserRelated.postValue(_questUserRelated)
            }
        }
    }

    /**
     * Добавить квест в таблицу квестов, выбранных текущим пользователем
     */
    fun appendQuestToUserQuests(quest: Quest) {
        val questUser = QuestUser(0, quest.id, currentUser.id)
        saveQuestUserToDB(questUser)
        _questUserRelated.add(QuestUserRelated(questUser, currentUser, quest))
        questUserRelated.value = _questUserRelated

        removeQuestFromAvailable(quest)
    }

    /**
     * Убрать квест из списка доступных
     */
    private fun removeQuestFromAvailable(quest: Quest) {
        val questAvailableUpdated = mutableListOf<Quest>()

        repeat(_questsAvailable.size) {
            if (_questsAvailable[it].id != quest.id) {
                questAvailableUpdated.add(_questsAvailable[it])
            }
        }

        _questsAvailable = questAvailableUpdated
        questsAvailable.value =_questsAvailable
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