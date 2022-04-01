package com.example.kvest2.data.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun readAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE login LIKE :l LIMIT 1")
    fun findByLogin(l: String): List<User?>
}