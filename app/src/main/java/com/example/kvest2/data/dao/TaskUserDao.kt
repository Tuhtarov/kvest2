package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.TaskUser

@Dao
interface TaskUserDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun addUser(quest: Quest)
//
    @Query("SELECT * FROM ${TaskUser.TABLE_NAME} ORDER BY id ASC")
    fun readAll(): List<TaskUser>
}