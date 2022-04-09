package com.example.kvest2.ui.quest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.entity.*
import com.example.kvest2.data.model.TaskUserRelatedStore
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository
import com.example.kvest2.data.repository.TaskQuestRepository
import com.example.kvest2.data.repository.TaskUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для фрагментов списка пользовательских квестов
 * и для добавления квеста (Quest) в пользовательские квесты (QuestUser)
 */
class QuestSharedViewModel (
    private val currentUser: User,
    private val questRepository: QuestRepository,
    private val questUserRepository: QuestUserRepository,
    private val taskUserRepository: TaskUserRepository,
    private val taskQuestRepository: TaskQuestRepository,
    private val taskUserRelatedStore: TaskUserRelatedStore
) : ViewModel() {

    private lateinit var _questsAvailable: MutableList<Quest>
    private lateinit var _questUserRelated: MutableList<QuestUserRelated>
    private lateinit var _userTasks: MutableList<TaskUserRelated>
    private lateinit var _questTasks: MutableList<TaskQuestRelated>

    val questsAvailable = MutableLiveData<MutableList<Quest>>()
    val questUserRelated = MutableLiveData<MutableList<QuestUserRelated>>()
    val userTasks = MutableLiveData<MutableList<TaskUserRelated>>()
    val questTasks = MutableLiveData<MutableList<TaskQuestRelated>>()

    init {
        // инициализируем листы, заполняем данными из БД
        viewModelScope.launch {
            // получаем доступные квесты для добавления текущим пользователем
            launch {
                _questsAvailable = questRepository.findAvailableByUserId(currentUser.id)
                questsAvailable.postValue(_questsAvailable)
            }

            // получаем пользовательские квесты для прохождения заданий
            launch {
                _questUserRelated = questUserRepository.findAllByUserId(currentUser.id)
                questUserRelated.postValue(_questUserRelated)
            }

            // получаем все квестовые задачи
            launch {
                _questTasks = taskQuestRepository.readAll()
                questTasks.postValue(_questTasks)
            }

            // получаем пользовательские задачи
            launch {
                _userTasks = taskUserRepository.readAllByUserId(currentUser.id)
                userTasks.postValue(_userTasks)

                taskUserRelatedStore.setUserTasks(_userTasks)
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
    fun changeQuestStatus(quest: QuestUserRelated, isCurrent: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // снимаем статус "текущего" квеста, со всех пользовательских квестов
            questUserRepository.clearCurrentQuestsByUserId(quest.questUser.userId)

            // назначаем пользовательский квест как "текущий", и сохраняем в БД
            questUserRepository.setCurrent(quest.questUser, isCurrent)

            _questUserRelated = questUserRepository.findAllByUserId(quest.questUser.userId)

            questUserRelated.postValue(_questUserRelated)
        }
    }
}