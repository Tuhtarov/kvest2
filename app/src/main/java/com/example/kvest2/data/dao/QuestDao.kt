package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kvest2.data.entity.Quest

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addQuest(quest: Quest)

    @Query("SELECT * FROM ${Quest.TABLE_NAME} ORDER BY created_at ASC")
    fun readAll(): LiveData<List<Quest>>

    @Query("DELETE FROM ${Quest.TABLE_NAME}")
    fun deleteAll()
}