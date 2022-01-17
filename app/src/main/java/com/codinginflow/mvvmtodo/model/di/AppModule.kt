package com.codinginflow.mvvmtodo.model.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.model.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {  //TODO: Dagger - Dependecy Injection - Object - Module

    @Provides
    @Singleton
    fun provideDatabase(
            app: Application,
            callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton                      //TODO: Coroutine
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope   //TODO: Annotations - evitar ambiguidade em casos de multiplos CoroutineScopes