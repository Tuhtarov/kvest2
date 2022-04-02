package com.example.kvest2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kvest2.data.entity.User
import com.example.kvest2.data.entity.UserDao

val recreateUserTable = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS ${User.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${User.TABLE_NAME} (`id` INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL)")
    }
}

val addColumnIsLoggedToUserTable = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${User.TABLE_NAME} ADD is_logged INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(
    entities = [User::class],
    version = 3,
    exportSchema = false
)
abstract class QuestDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: QuestDatabase? = null

        fun getDatabase(context: Context): QuestDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestDatabase::class.java,
                    "quest_database"
                )
                    .addMigrations(recreateUserTable, addColumnIsLoggedToUserTable)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}