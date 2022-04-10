package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kvest2.data.entity.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>): Array<Long>

    @Query("SELECT * FROM ${Task.TABLE_NAME} ORDER BY id ASC")
    fun readAll(): LiveData<List<Task>>
}