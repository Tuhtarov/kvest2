package com.example.kvest2.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.model.AppUserSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository

class QuestViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestSharedViewModel::class.java)) {
            val db = QuestDatabase.getDatabase(context)
            val questDao = db.questDao()
            val questUserDao = db.questUserDao()
            val questUserRelatedDao = db.questUserRelated()

            return QuestSharedViewModel (
                QuestRepository(questDao),
                QuestUserRepository(questUserDao, questUserRelatedDao),
                AppUserSingleton.getUser()!!
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}