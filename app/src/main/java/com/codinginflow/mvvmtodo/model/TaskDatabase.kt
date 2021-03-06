package com.codinginflow.mvvmtodo.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.model.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
            private val database: Provider<TaskDatabase>, //TODO: Evitar dependencia circular
            @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db operations
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes", priority = true, "wash the plates and cups", completed = false)) //Testando as tasks
                dao.insert(Task("Call Mom", priority = true, "(19)999999999", completed = true))
                dao.insert(Task("Terminar o app", priority = false, "Corrigir os bugs", completed = false))
                dao.insert(Task("Estudar Kotlin", priority = false, "", completed = true))
            }
        }
    }
}
