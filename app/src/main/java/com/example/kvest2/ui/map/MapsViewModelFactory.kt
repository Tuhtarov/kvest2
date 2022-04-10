package com.example.kvest2.ui.map

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

class MapsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = QuestDatabase.getDatabase(context)

        val questDao = db.questDao()
        val questUserRelatedDao = db.questUserRelated()
        val taskDao = db.taskDao()
        val taskAnswerDao = db.taskAnswerRelated()

        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel (
                currentUser = AppUserSingleton.user.value!!,
                questRepository = QuestRepository(questDao, questUserRelatedDao),
                taskRepository = TaskRepository(taskDao, taskAnswerDao),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}