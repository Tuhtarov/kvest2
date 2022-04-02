package com.example.kvest2.data.entity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun readAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE name LIKE :u LIMIT 1")
    fun findByUsername(u: String): List<User>

    @Query("SELECT * FROM user WHERE is_logged LIKE 1 ORDER BY id DESC LIMIT 1")
    fun findLoggedUser(): List<User>

    @Update
    suspend fun updateUser(user: User)
}