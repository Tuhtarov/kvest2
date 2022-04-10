package com.example.kvest2.data.model

import androidx.lifecycle.MutableLiveData
import com.example.kvest2.data.entity.QuestUserRelated
import com.example.kvest2.data.entity.TaskAnswerRelated
import com.example.kvest2.data.entity.TaskUserRelated

object AppCurrentTasksSingleton {
    var currentQuest = MutableLiveData<QuestUserRelated>()
    var currentTasks = MutableLiveData<MutableList<TaskAnswerRelated>>()
}
