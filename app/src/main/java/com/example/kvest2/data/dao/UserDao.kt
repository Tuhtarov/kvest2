package com.example.kvest2.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kvest2.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM ${User.TABLE_NAME} ORDER BY id ASC")
    fun readAll(): LiveData<List<User>>

    @Query("SELECT * FROM ${User.TABLE_NAME} WHERE name LIKE :u LIMIT 1")
    fun findByUsername(u: String): List<User>

    @Query("SELECT * FROM ${User.TABLE_NAME} WHERE is_logged LIKE 1 ORDER BY id DESC LIMIT 1")
    fun findLoggedUser(): List<User>

    @Update
    suspend fun updateUser(user: User)
}