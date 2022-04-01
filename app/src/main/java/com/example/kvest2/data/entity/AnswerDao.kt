package com.example.kvest2.data.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(answerDao: AnswerDao)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun readAll(): LiveData<List<Answer>>
}