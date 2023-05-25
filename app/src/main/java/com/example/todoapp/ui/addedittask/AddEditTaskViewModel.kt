package com.example.todoapp.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.task.TasksViewModel

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val tasks = state.get<Task>("task")


    class AddEditTaskViewModelFactory(private val repository: TaskRepository, private val state: SavedStateHandle) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddEditTaskViewModel(repository, state) as T
        }
    }
}