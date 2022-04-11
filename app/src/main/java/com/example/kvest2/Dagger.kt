package com.example.kvest2

import com.example.kvest2.data.api.AnswerRemoteRepository
import com.example.kvest2.data.api.QuestRemoteRepository
import com.example.kvest2.data.api.TaskRemoteRepository
import com.example.kvest2.data.dao.*
import com.example.kvest2.data.db.QuestDatabase
import com.example.kvest2.data.repository.*
import com.example.kvest2.ui.MainActivity
import com.example.kvest2.ui.MainActivityViewModel
import com.example.kvest2.ui.home.HomeViewModel
import com.example.kvest2.ui.quest.QuestSharedViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [
    AppDaoModule::class
])
@Singleton
interface AppComponent {
    val taskRemoteRepository: TaskRemoteRepository
    val questRemoteRepository: QuestRemoteRepository
    val answerRemoteRepository: AnswerRemoteRepository

    val questRepository: QuestRepository
    val questUserRepository: QuestUserRepository
    val taskUserRepository: TaskUserRepository
    val taskQuestRepository: TaskQuestRepository
    val taskRepository: TaskRepository
    val answerRepository: AnswerRepository
    val userRepository: UserRepository

    fun inject(activity: MainActivity)

    fun inject(viewModel: QuestSharedViewModel)

    fun inject(viewModel: MainActivityViewModel)

    fun inject(viewModel: HomeViewModel)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun database(db: QuestDatabase): Builder

        fun build(): AppComponent
    }
}


@Module
class AppDaoModule {

    @Provides
    fun getQuestDao(db: QuestDatabase): QuestDao {
        return db.questDao()
    }

    @Provides
    fun questUserDao(db: QuestDatabase): QuestUserDao {
        return db.questUserDao()
    }

    @Provides
    fun questUserRelated(db: QuestDatabase): QuestUserRelatedDao {
        return db.questUserRelated()
    }

    @Provides
    fun taskQuestRelated(db: QuestDatabase): TaskQuestRelatedDao {
        return db.taskQuestRelated()
    }

    @Provides
    fun taskUserRelated(db: QuestDatabase): TaskUserRelatedDao {
        return db.taskUserRelated()
    }

    @Provides
    fun taskDao(db: QuestDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    fun taskUserDao(db: QuestDatabase): TaskUserDao {
        return db.taskUserDao()
    }

    @Provides
    fun taskAnswerRelatedDao(db: QuestDatabase): TaskAnswerRelatedDao {
        return db.taskAnswerRelated()
    }

    @Provides
    fun userDao(db: QuestDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun answerDao(db: QuestDatabase): AnswerDao {
        return db.answerDao()
    }
}

