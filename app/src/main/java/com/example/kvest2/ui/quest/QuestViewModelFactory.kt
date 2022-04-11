package com.example.kvest2.ui.quest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kvest2.data.model.AppUserSingleton

class QuestViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestSharedViewModel::class.java)) {
            return QuestSharedViewModel(
                context = context,
                currentUser = AppUserSingleton.user.value!!,
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}