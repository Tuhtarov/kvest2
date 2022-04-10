package com.example.kvest2.ui.quest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.api.QuestApi
import com.example.kvest2.data.api.QuestRemoteRepository
import com.example.kvest2.data.entity.*
import com.example.kvest2.data.model.ApiFetchResult
import com.example.kvest2.data.model.ApiResult
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.model.TaskUserRelatedStore
import com.example.kvest2.data.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для фрагментов списка пользовательских квестов
 * и для добавления квеста (Quest) в пользовательские квесты (QuestUser)
 */
class QuestSharedViewModel(
    private val currentUser: User,
    private val questRepository: QuestRepository,
    private val questUserRepository: QuestUserRepository,
    private val taskUserRepository: TaskUserRepository,
    private val taskQuestRepository: TaskQuestRepository,
    private val taskUserRelatedStore: TaskUserRelatedStore,
    private val questRemoteRepository: QuestRemoteRepository,
    private val answerRepository: AnswerRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private lateinit var _questsAvailable: MutableList<Quest>
    private lateinit var _questUserRelated: MutableList<QuestUserRelated>

    val questsAvailable = MutableLiveData<MutableList<Quest>>()
    val questUserRelated = MutableLiveData<MutableList<QuestUserRelated>>()

    val fetchQuestIsLoading = MutableLiveData<Boolean?>(null)
    val apiFetchResult = MutableLiveData<ApiResult>()

    init {
        // инициализируем листы, заполняем данными из БД
        viewModelScope.launch(Dispatchers.IO) {
            // получаем доступные квесты для добавления текущим пользователем
            launch {
                fetchQuestsFromApi()
            }
                .invokeOnCompletion {
                    launch {
                        getAvailableQuests()
                    }
                }

            // получаем пользовательские квесты для прохождения заданий
            launch {
                loadQuestsForCurrentUser()
            }

            launch {
                findCurrentTasks()
            }
        }
    }

    private suspend fun getAvailableQuests() {
        _questsAvailable = questRepository.findAvailableByUserId(currentUser.id)
        questsAvailable.postValue(_questsAvailable)
    }

    private suspend fun findCurrentTasks() {
        val quest = questRepository.findCurrentQuestByUserId(currentUser.id)

        val tasks = if (quest != null)
            taskRepository.getAllRelatedByQuestId(quest.quest.id) else null

        AppCurrentTasksSingleton.currentTasks.postValue(tasks)
        AppCurrentTasksSingleton.currentQuest.postValue(quest)
    }

    private suspend fun fetchQuestsFromApi() {
        fetchQuestIsLoading.postValue(true)

        try {
            val response = questRemoteRepository.getQuests()

            if (response.isSuccessful) {
                handleFetchedQuests(response.body())
            }
        } catch (e: Exception) {
            handleServerError(e)
        }

        fetchQuestIsLoading.postValue(false)
    }

    private suspend fun handleFetchedQuests(questsApi: MutableList<QuestApi>?) {
        if (questsApi.isNullOrEmpty()) {
            apiFetchResult.postValue(
                ApiFetchResult("У сервера отсутствуют квесты!", false)
            )

            return
        }

        val fetchedQuests = questRemoteRepository.getQuestsFromQuestsListApi(questsApi)

        val fetchedTasks =
            questRemoteRepository.taskRemoteRepository.getTasksFromQuestListApi(questsApi)

        val fetchedAnswers =
            questRemoteRepository.taskRemoteRepository.answerRemoteRepository
                .getAnswersFromTaskListApi(questsApi)

        questRepository.insertQuests(fetchedQuests)
        answerRepository.insertAnswers(fetchedAnswers)
        taskRepository.insertTasks(fetchedTasks)
    }

    private fun handleServerError(error: Exception) {
        apiFetchResult.postValue(
            ApiFetchResult(error.message.toString(), true)
        )
    }

    /**
     * Добавить квест в таблицу квестов, выбранных текущим пользователем
     */
    fun appendQuestToUserQuests(quest: Quest) {
        val questUser = QuestUser(0, quest.id, currentUser.id)

        viewModelScope.launch {
            questUserRepository.addQuestUser(questUser)
            loadQuestsForCurrentUser()
            getAvailableQuests()
        }
    }

    private suspend fun loadQuestsForCurrentUser() {
        _questUserRelated = questUserRepository.findAllByUserId(currentUser.id)

        questUserRelated.postValue(_questUserRelated)
    }


    /**
     * Обозначить пользовательский квест как текущий
     */
    fun changeQuestStatus(quest: QuestUserRelated, isCurrent: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            questUserRepository.clearCurrentQuestsByUserId(quest.questUser.userId)
            questUserRepository.setCurrentQuest(quest.questUser, isCurrent)

            loadQuestsForCurrentUser()
            findCurrentTasks()
        }
    }
}