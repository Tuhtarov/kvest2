package com.example.kvest2.ui.map

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.model.AppCurrentTasksSingleton
import com.example.kvest2.data.repository.QuestRepository
import com.example.kvest2.data.repository.TaskRepository
import com.example.kvest2.databinding.QuestFragmentBinding
import com.example.kvest2.ui.quest.QuestSharedViewModel
import com.example.kvest2.ui.quest.QuestViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(
    private val questRepository: QuestRepository,
    private val taskRepository: TaskRepository,
    private val currentUser: User
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val currentQuest = questRepository.findCurrentQuestByUserId(currentUser.id)

            if (currentQuest != null) {
                val tasks = taskRepository.getAllRelatedByQuestId(currentQuest.quest.id)

                AppCurrentTasksSingleton.currentTasks.postValue(tasks)
                AppCurrentTasksSingleton.currentQuest.postValue(currentQuest)
            }
        }
    }
}