package com.example.todoapp.ui.deleteallcompleted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.task.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteAllCompletedViewModel(
    private val taskRepository: TaskRepository,
    private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmClick() {
        applicationScope.launch {
            taskRepository.deleteAllCompletedTasks()
        }
    }

    class DeleteAllCompletedViewModelFactory(
        private val repository: TaskRepository,
        private val applicationScope: CoroutineScope
        ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DeleteAllCompletedViewModel(repository, applicationScope) as T
        }
    }
}