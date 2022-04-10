package com.example.kvest2.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.TaskRepository
import com.example.kvest2.data.repository.UserRepository

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val db = QuestDatabase.getDatabase(context = context)
            val userDao = db.userDao()
            val questDao = db.questDao()
            val questUserRelatedDao = db.questUserRelated()
            val taskDao = db.taskDao()
            val taskAnswerDao = db.taskAnswerRelated()

            return HomeViewModel (
                userRepository = UserRepository(userDao),
                currentUser = AppUserSingleton.getUser()!!,
                questRepository = QuestRepository(questDao, questUserRelatedDao),
                taskRepository = TaskRepository(taskDao, taskAnswerDao),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}