package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.QuestUserRelated

@Dao
interface QuestUserRelatedDao {
    @Query("SELECT DISTINCT * FROM ${QuestUser.TABLE_NAME} ORDER BY id DESC")
    suspend fun findAll(): MutableList<QuestUserRelated>

    @Query("SELECT DISTINCT * FROM ${QuestUser.TABLE_NAME} WHERE `user_id` = :userId ORDER BY id DESC")
    suspend fun findAllByUserId(userId: Int): MutableList<QuestUserRelated>
}