package com.example.kvest2.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.model.AppTaskUserSingleton
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository
import com.example.kvest2.data.repository.TaskQuestRepository
import com.example.kvest2.data.repository.TaskUserRepository

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

            return QuestSharedViewModel (
                currentUser = AppUserSingleton.getUser()!!,
                questRepository =  QuestRepository(questDao),
                questUserRepository =  QuestUserRepository(questUserDao, questUserRelatedDao),
                taskQuestRepository = TaskQuestRepository(taskQuestRelatedDao),
                taskUserRepository = TaskUserRepository(taskUserRelatedDao),
                taskUserRelatedStore = AppTaskUserSingleton
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}