package com.example.kvest2.data.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(task: Task)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun readAll(): LiveData<List<Task>>
}