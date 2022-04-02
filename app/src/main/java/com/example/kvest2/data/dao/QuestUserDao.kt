package com.example.kvest2.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kvest2.data.entity.QuestUser

@Dao
interface QuestUserDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun addUser(quest: Quest)
//
    @Query("SELECT * FROM ${QuestUser.TABLE_NAME} ORDER BY id ASC")
    fun readAll(): List<QuestUserDao>
}