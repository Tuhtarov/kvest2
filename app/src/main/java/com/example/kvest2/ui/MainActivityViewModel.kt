package com.example.kvest2.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.appComponent
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.TaskRepository
import com.example.kvest2.data.repository.TaskUserRepository
import com.example.kvest2.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel(context: Context) : ViewModel() {
    @Inject
    lateinit var questRepository: QuestRepository

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var taskUserRepository: TaskUserRepository

    val loggedUser = MutableLiveData<User>()

    init {
        context.applicationContext.appComponent.inject(this)

        viewModelScope.launch(Dispatchers.IO) {
            loadUserData()
        }
    }

    /**
     * Загрузить пользовательские задачи, пользовательский квест
     */
    private suspend fun loadUserData() {
        val currentUser = userRepository.findLoggedUser().user

        if (currentUser != null) {
            loggedUser.postValue(currentUser!!)
            AppUserSingleton.user.postValue(currentUser)
            loadQuestAndTasksByUser(currentUser)
        } else {
            Log.e("user-error", "Текущий пользователь не найден")
        }
    }

    private suspend fun loadQuestAndTasksByUser(user: User) {
        val userQuest = questRepository.findCurrentQuestByUserId(user.id)

        if (userQuest != null) {
            val tasks = taskRepository.getAllRelatedByQuestId(userQuest.quest.id)
            val tasksUser = taskUserRepository.readCurrentByUserAndQuest(user, userQuest.quest)

            AppCurrentTasksSingleton.apply {
                currentQuest.postValue(userQuest.quest)
                currentTasks.postValue(tasks)
                currentTasksUser.postValue(tasksUser)

                var taskAnswer: TaskAnswerRelated? = null

                Log.i("current-tasks", "Количество текущих задач ${tasks?.size}")
                Log.i(
                    "current-tasks",
                    "Количество пользовательских текущих задач ${tasksUser?.size}"
                )
                Log.i("current-quest", "Текущий квест ${userQuest.quest.name}")

                tasksUser.forEach { t ->
                    if (t.taskUser.isCurrent) {
                        Log.i("current-task", "Найдена текущая задача ${t.task.question}")

                        taskAnswer = taskRepository.getTaskWithAnswerFromCurrentTask(t.task)
                        currentTask.postValue(taskAnswer)
                    }
                }

                if (taskAnswer == null && tasks.isNotEmpty()) {
                    val firstCurrentTask = tasks[0]

                    currentTask.postValue(firstCurrentTask)
                    taskRepository.clearCurrentTasks(user)
                    taskRepository.saveToTaskUser(firstCurrentTask.task, user, true)

                    Log.i(
                        "current-task", "Автоматически получена текущая" +
                                " задача (${firstCurrentTask.task.question}) и сохранена"
                    )
                } else {
                    Log.e("current-tasks", "Текущая задача = null или список текущих задач пуст")
                }
            }
        } else {
            Log.e("current-quest-error", "Текущий пользовательский квест не найден")
        }
    }
}