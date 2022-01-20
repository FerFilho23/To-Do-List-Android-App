package com.codinginflow.mvvmtodo.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.model.Task
import com.codinginflow.mvvmtodo.model.TaskDao
import com.codinginflow.mvvmtodo.view.ADD_TASK_RESULT_OK
import com.codinginflow.mvvmtodo.view.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
        private val taskDao: TaskDao,
        @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskDescription = state.get<String>("taskDescription") ?: task?.description ?: ""
        set(value) {
            field = value
            state.set("taskDescription", value)
        }

    var taskPriority = state.get<Boolean>("taskPriority") ?: task?.priority ?: false
        set(value) {
            field = value
            state.set("taskPriority", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage()
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, priority = taskPriority, taskDescription)
            updateTask(updatedTask)
        } else {
            val newTask = Task(taskName, taskPriority, taskDescription)
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage()  = viewModelScope.launch{
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage("Name cannot be empty"))
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}