package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.QuestUserRelated

@Dao
interface QuestUserRelatedDao {
    @Query("SELECT * from ${QuestUser.TABLE_NAME}")
    suspend fun findAll(): MutableList<QuestUserRelated>

    @Query("SELECT * from ${QuestUser.TABLE_NAME} where `user_id` = :userId")
    suspend fun findAllByUserId(userId: Int): MutableList<QuestUserRelated>
}