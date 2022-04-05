package com.example.kvest2.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.QuestUserRepository

class QuestViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            val questDao = QuestDatabase.getDatabase(context).questDao()
            val questUserDao = QuestDatabase.getDatabase(context).questUserDao()

            return QuestViewModel (
                QuestRepository(questDao),
                QuestUserRepository(questUserDao)
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}