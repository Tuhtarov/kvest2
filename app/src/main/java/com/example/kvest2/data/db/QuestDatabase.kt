package com.example.kvest2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kvest2.data.dao.*
import com.example.kvest2.data.entity.*
import dagger.Provides
import javax.inject.Inject

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

val addNewColumns = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // КВЕСТЫ
        database.execSQL("DROP TABLE IF EXISTS ${Quest.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${Quest.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL," +
                " `name` TEXT NOT NULL," +
                " `description` TEXT NOT NULL," +
                " `created_at` TEXT NOT NULL)")

        // ОТВЕТЫ
        database.execSQL("DROP TABLE IF EXISTS ${Answer.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${Answer.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL, `text` TEXT NOT NULL)")

        // ЗАДАЧИ
        database.execSQL("DROP TABLE IF EXISTS ${Task.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${Task.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL," +
                " `quest_id` INTEGER," +
                " `correct_answer_id` INTEGER NOT NULL," +
                " `question` TEXT NOT NULL," +
                " `latitude` TEXT NOT NULL," +
                " `longitude` TEXT NOT NULL," +
                " `score` INTEGER," +
                " `priority` INTEGER," +
                " FOREIGN KEY(quest_id) REFERENCES ${Quest.TABLE_NAME}(id)," +
                " FOREIGN KEY(correct_answer_id) REFERENCES ${Answer.TABLE_NAME}(id))")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_correct_answer_id ON ${Task.TABLE_NAME} (correct_answer_id);")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_quest_id ON ${Task.TABLE_NAME} (quest_id);")

        // ЗАДАЧИ ПОЛЬЗОВАТЕЛЯ
        database.execSQL("DROP TABLE IF EXISTS ${TaskUser.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${TaskUser.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL," +
                " `task_id` INTEGER NOT NULL," +
                " `user_id` INTEGER NOT NULL," +
                " `is_answered` INTEGER NOT NULL DEFAULT 0," +
                " FOREIGN KEY(task_id) REFERENCES ${Task.TABLE_NAME}(id)," +
                " FOREIGN KEY(user_id) REFERENCES ${User.TABLE_NAME}(id))")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_user_user_id ON ${TaskUser.TABLE_NAME} (user_id);")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_user_task_id ON ${TaskUser.TABLE_NAME} (task_id);")

        // КВЕСТЫ ПОЛЬЗОВАТЕЛЯ
        database.execSQL("DROP TABLE IF EXISTS ${QuestUser.TABLE_NAME}")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${QuestUser.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL," +
                " `quest_id` INTEGER NOT NULL," +
                " `user_id` INTEGER NOT NULL," +
                " `is_current` INTEGER NOT NULL DEFAULT 0," +
                " FOREIGN KEY(quest_id) REFERENCES ${Quest.TABLE_NAME}(id)," +
                " FOREIGN KEY(user_id) REFERENCES ${User.TABLE_NAME}(id))")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_quest_user_user_id ON ${QuestUser.TABLE_NAME} (user_id);")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_quest_user_quest_id ON ${QuestUser.TABLE_NAME} (quest_id);")
    }
}

val addNewIndexes = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // пользовательские квесты
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_quest_user_user_id_quest_id" +
                " ON ${QuestUser.TABLE_NAME} (user_id, quest_id);")

        // пользовательские задачи
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_task_user_task_id_user_id" +
                " ON ${TaskUser.TABLE_NAME} (user_id, task_id);")
    }
}

val addCascadeOnDeleteAndUpdateToTask = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS ${Task.TABLE_NAME}")

        database.execSQL("CREATE TABLE IF NOT EXISTS ${Task.TABLE_NAME} " +
                "(`id` INTEGER PRIMARY KEY NOT NULL," +
                " `quest_id` INTEGER," +
                " `correct_answer_id` INTEGER NOT NULL," +
                " `question` TEXT NOT NULL," +
                " `latitude` TEXT NOT NULL," +
                " `longitude` TEXT NOT NULL," +
                " `score` INTEGER," +
                " `priority` INTEGER," +
                " FOREIGN KEY(quest_id) REFERENCES ${Quest.TABLE_NAME}(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                " FOREIGN KEY(correct_answer_id) REFERENCES ${Answer.TABLE_NAME}(id) ON DELETE CASCADE ON UPDATE CASCADE)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_correct_answer_id ON ${Task.TABLE_NAME} (correct_answer_id);")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_task_quest_id ON ${Task.TABLE_NAME} (quest_id);")
    }
}

val addIsCurrentToTask = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${TaskUser.TABLE_NAME} ADD is_current INTEGER NOT NULL DEFAULT 0")
    }
}


@Database (
    entities = [
        User::class,
        Quest::class,
        Answer::class,
        Task::class,
        TaskUser::class,
        QuestUser::class,
    ],
    version = 7,
    exportSchema = false
)
abstract class QuestDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao

    abstract fun answerDao(): AnswerDao

    abstract fun questDao(): QuestDao

    abstract fun questUserDao(): QuestUserDao

    /**
     * Сущности с имплиментом связанных сущностей
     */
    abstract fun questUserRelated(): QuestUserRelatedDao

    abstract fun taskUserRelated(): TaskUserRelatedDao

    abstract fun taskQuestRelated(): TaskQuestRelatedDao

    abstract fun taskAnswerRelated(): TaskAnswerRelatedDao

    abstract fun taskUserDao(): TaskUserDao

    companion object {
        @Volatile
        private var INSTANCE: QuestDatabase? = null

        fun getDatabase(context: Context): QuestDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder (
                    context.applicationContext,
                    QuestDatabase::class.java,
                    "quest_database"
                )
                    .addMigrations (
                        recreateUserTable,
                        addColumnIsLoggedToUserTable,
                        addNewColumns,
                        addNewIndexes,
                        addCascadeOnDeleteAndUpdateToTask,
                        addIsCurrentToTask
                    ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}