package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kvest2.data.entity.QuestUser

@Dao
interface QuestUserDao {

    @Query("SELECT * FROM ${QuestUser.TABLE_NAME} ORDER BY id ASC")
    fun readAll(): LiveData<List<QuestUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestUser(questUser: QuestUser)

    @Query("UPDATE ${QuestUser.TABLE_NAME} SET `is_current` = 0 WHERE `user_id` = :userId AND `is_current` = 1")
    suspend fun clearCurrentQuestsByUser(userId: Int)

    @Query(
        "UPDATE ${QuestUser.TABLE_NAME} SET `is_current` = :status" +
                " WHERE `user_id` = :userId AND `id` = :questUserId"
    )
    suspend fun setCurrent(userId: Int, questUserId: Int, status: Int)
}