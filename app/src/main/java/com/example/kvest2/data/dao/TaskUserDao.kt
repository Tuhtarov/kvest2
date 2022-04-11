package com.example.kvest2.data.dao

import androidx.room.*
import com.example.kvest2.data.entity.QuestUser
import com.example.kvest2.data.entity.TaskUser

@Dao
interface TaskUserDao {
    @Query("SELECT * FROM ${TaskUser.TABLE_NAME} ORDER BY id ASC")
    suspend fun readAll(): List<TaskUser>

    @Query("UPDATE ${TaskUser.TABLE_NAME} SET `is_current` = 0" +
            " WHERE `user_id` = :userId AND `is_current` = 1")
    suspend fun clearCurrentTasks(userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskUser: TaskUser)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskUser: TaskUser)
}