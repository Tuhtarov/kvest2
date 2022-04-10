package com.example.kvest2.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.api.AnswerRemoteRepository
import com.example.kvest2.data.api.QuestRemoteRepository
import com.example.kvest2.data.api.TaskRemoteRepository
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.model.AppTaskUserSingleton
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.*

class QuestViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestSharedViewModel::class.java)) {
            val db = QuestDatabase.getDatabase(context)

            val questDao = db.questDao()
            val questUserDao = db.questUserDao()
            val questUserRelatedDao = db.questUserRelated()
            val taskQuestRelatedDao = db.taskQuestRelated()
            val taskUserRelatedDao = db.taskUserRelated()
            val taskDao = db.taskDao()
            val answerDao = db.answerDao()
            val taskAnswerDao = db.taskAnswerRelated()

            return QuestSharedViewModel(
                currentUser = AppUserSingleton.user.value!!,
                questRepository = QuestRepository(questDao, questUserRelatedDao),
                questRemoteRepository = QuestRemoteRepository(
                    TaskRemoteRepository(
                        AnswerRemoteRepository()
                    )
                ),
                questUserRepository = QuestUserRepository(questUserDao, questUserRelatedDao),
                taskQuestRepository = TaskQuestRepository(taskQuestRelatedDao),
                taskUserRepository = TaskUserRepository(taskUserRelatedDao),
                taskUserRelatedStore = AppTaskUserSingleton,
                taskRepository = TaskRepository(taskDao, taskAnswerDao),
                answerRepository = AnswerRepository(answerDao)
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}