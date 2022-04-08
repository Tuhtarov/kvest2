package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kvest2.data.entity.Quest
import com.example.kvest2.data.entity.QuestUser

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addQuest(quest: Quest)

    @Query("SELECT * FROM ${Quest.TABLE_NAME} ORDER BY created_at ASC")
    fun readAll(): LiveData<List<Quest>>

    @Query("DELETE FROM ${Quest.TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT * FROM ${Quest.TABLE_NAME} WHERE `id` = :id")
    fun findById(id: Int): Quest?

    @Query("SELECT q.* FROM quest AS q JOIN quest_user AS qu ON qu.quest_id = q.id" +
            " WHERE qu.quest_id NOT IN (SELECT quest_id FROM quest_user WHERE user_id = :userId)")
    suspend fun findAvailableByUserId(userId: Int): MutableList<Quest>
}