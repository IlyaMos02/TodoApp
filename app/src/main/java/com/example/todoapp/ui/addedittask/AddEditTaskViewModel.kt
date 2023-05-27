package com.example.todoapp.ui.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.task.TasksViewModel

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    fun onSaveClick(task: Task){

    }

    class AddEditTaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddEditTaskViewModel(repository) as T
        }
    }
}