package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = User.TABLE_NAME)
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,

    @ColumnInfo(name = "is_logged")
    val isLogged: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "user"
    }
}
