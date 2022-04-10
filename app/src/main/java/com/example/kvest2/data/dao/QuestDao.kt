package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kvest2.data.entity.Quest

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuest(quest: Quest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<Quest>): Array<Long>

    @Query("SELECT * FROM ${Quest.TABLE_NAME} ORDER BY created_at ASC")
    fun readAll(): LiveData<List<Quest>>

    @Query("DELETE FROM ${Quest.TABLE_NAME}")
    fun deleteAll()

    @Query("SELECT * FROM ${Quest.TABLE_NAME} WHERE `id` = :id")
    fun findById(id: Int): Quest?

    @Query("SELECT q.* FROM quest AS q" +
            " LEFT OUTER JOIN quest_user AS qu ON qu.quest_id = q.id" +
            " WHERE q.id NOT IN (SELECT quest_id FROM quest_user WHERE user_id = :userId)")
    suspend fun findAvailableByUserId(userId: Int): MutableList<Quest>

    @Query("SELECT * FROM quest " +
            "WHERE `name` = :name and `description` = :description and `created_at` = :createdAt ")
    suspend fun findQuests(name: String, description: String, createdAt: String): MutableList<Quest>
}