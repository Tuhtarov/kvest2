package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.AnswerDao
import com.example.kvest2.data.entity.Answer
import javax.inject.Inject

class AnswerRepository @Inject constructor(private val dao: AnswerDao) {
    suspend fun insertAnswers(quests: List<Answer>): Array<Long> {
        return dao.insertAnswers(quests)
    }
}