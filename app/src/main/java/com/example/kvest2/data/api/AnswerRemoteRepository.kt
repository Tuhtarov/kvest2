package com.example.kvest2.data.api

import com.example.kvest2.data.entity.Answer
import javax.inject.Inject

class AnswerRemoteRepository @Inject constructor() {
    fun getAnswersFromTaskListApi(questsApi: List<QuestApi>): List<Answer> {
        val answers = mutableListOf<Answer>()

        questsApi.forEach {
            it.tasks.forEach { t ->
                if (t.answer != null) {
                    answers.add(t.answer!!)
                }
            }
        }

        return answers
    }
}