package com.example.todoapp.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.ADD_TASK_RESULT_OK
import com.example.todoapp.ui.EDIT_TASK_RESULT_OK
import com.example.todoapp.ui.task.TasksViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick(task: Task){
        if (task.name.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if(task.uid != ""){
            val updatedTask = task.copy()
            updateTask(updatedTask)
        } else {
            val newTask = task.copy(user_id = FirebaseAuth.getInstance().currentUser!!.uid)
            createTask(newTask)
        }
    }

    private fun createTask(newTask: Task) {
        viewModelScope.launch {
            taskRepository.createTask(newTask)
            addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
        }
    }
    private fun updateTask(updatedTask: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(updatedTask)
            addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
        }
    }

    private fun showInvalidInputMessage(msg: String) {
        viewModelScope.launch {
            addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(msg))
        }
    }

    sealed class AddEditTaskEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int): AddEditTaskEvent()
    }

    class AddEditTaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddEditTaskViewModel(repository) as T
        }
    }
}