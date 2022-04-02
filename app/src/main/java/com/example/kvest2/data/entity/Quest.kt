package com.example.kvest2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Quest.TABLE_NAME)
data class Quest (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
) {
    companion object {
        const val TABLE_NAME = "quest"
    }
}

/**
 * @Documentation
 * createdAt - дата создания квеста
 */