package com.example.kvest2.data.repository

import com.example.kvest2.data.dao.AnswerDao
import com.example.kvest2.data.entity.Answer

class AnswerRepository(private val dao: AnswerDao) {
    suspend fun insertAnswers(quests: List<Answer>): Array<Long> {
        return dao.insertAnswers(quests)
    }
}