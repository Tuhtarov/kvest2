package com.example.kvest2.ui.quest

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.appComponent
import com.example.kvest2.data.api.AnswerRemoteRepository
import com.example.kvest2.data.api.QuestApi
import com.example.kvest2.data.api.QuestRemoteRepository
import com.example.kvest2.data.api.TaskRemoteRepository
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.QuestUserRelated
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.model.ApiFetchResult
import com.example.kvest2.data.model.ApiResult
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.repository.AnswerRepository
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository
import com.example.kvest2.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для фрагментов списка пользовательских квестов
 * и для добавления квеста (Quest) в пользовательские квесты (QuestUser)
 */
class QuestSharedViewModel(context: Context, val currentUser: User) : ViewModel() {
    @Inject
    lateinit var taskRemoteRepository: TaskRemoteRepository
    @Inject
    lateinit var questRemoteRepository: QuestRemoteRepository
    @Inject
    lateinit var answerRemoteRepository: AnswerRemoteRepository
    @Inject
    lateinit var questRepository: QuestRepository
    @Inject
    lateinit var questUserRepository: QuestUserRepository
    @Inject
    lateinit var taskRepository: TaskRepository
    @Inject
    lateinit var answerRepository: AnswerRepository

    private lateinit var _questsAvailable: MutableList<Quest>
    private lateinit var _questUserRelated: MutableList<QuestUserRelated>

    val questsAvailable = MutableLiveData<MutableList<Quest>>()
    val questUserRelated = MutableLiveData<MutableList<QuestUserRelated>>()

    val fetchQuestIsLoading = MutableLiveData<Boolean?>(null)
    val apiFetchResult = MutableLiveData<ApiResult>()

    init {
        context.applicationContext.appComponent.inject(this)

        // инициализируем листы, заполняем данными из БД
        viewModelScope.launch(Dispatchers.IO) {
            // получаем доступные квесты для добавления текущим пользователем
            launch {
                fetchQuestsFromApi()
            }
                .invokeOnCompletion {
                    launch {
                        getAvailableQuests()
                        loadQuestsForCurrentUser()
                        findCurrentTasks()
                    }
                }
        }
    }

    private suspend fun getAvailableQuests() {
        _questsAvailable = questRepository.findAvailableByUserId(currentUser.id)
        questsAvailable.postValue(_questsAvailable)
    }

    private suspend fun findCurrentTasks() {
        val questUser = questRepository.findCurrentQuestByUserId(currentUser.id)

        if (questUser != null) {
            val tasks = taskRepository.getAllRelatedByQuestId(questUser.quest.id)
            AppCurrentTasksSingleton.currentTasks.postValue(tasks)
            AppCurrentTasksSingleton.currentQuest.postValue(questUser.quest)
        } else {
            AppCurrentTasksSingleton.currentTasks.postValue(null)
            AppCurrentTasksSingleton.currentQuest.postValue(null)

            Log.e("current-tasks", "нет текущего квеста")
        }
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

        val fetchedTasks = taskRemoteRepository.getTasksFromQuestListApi(questsApi)

        val fetchedAnswers = answerRemoteRepository.getAnswersFromTaskListApi(questsApi)

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