package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.TaskUser
import com.example.kvest2.data.entity.TaskUserRelated

@Dao
interface TaskUserRelatedDao {

    @Query("SELECT * FROM ${TaskUser.TABLE_NAME} WHERE `user_id` = :userId")
    suspend fun readAllByUserId(userId: Int): MutableList<TaskUserRelated>
}