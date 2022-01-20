package com.codinginflow.mvvmtodo.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao { //TODO: DAO

    fun getTasks(query: String, sortOrder: com.codinginflow.mvvmtodo.model.SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
            when (sortOrder){
                SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
                SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
            }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY priority DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>    //TODO: estudar flow - asynchronous stream of data

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY priority DESC, createdTime")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)    //REPLACE the task that has the same id as the one being inserted
    suspend fun insert(task: Task) //TODO: estudar suspend - room nao deixa executar nenhuma operacao de DB na main thread.

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    suspend fun deleteCompletedTasks()
}