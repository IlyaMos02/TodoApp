package com.example.todoapp.ui.task

import androidx.lifecycle.*
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel(private val taskRepository: TaskRepository,
                     private val state:SavedStateHandle
                     ) : ViewModel() {
    val tasksLiveData = taskRepository.tasks

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    fun getTasks(name: String): List<Task>? {
        return if(name == ""){
            taskRepository.tasks.value
        } else{
            val returns = taskRepository.tasks.value?.map { it.copy() }
            returns?.filter {
                it.user_id == FirebaseAuth.getInstance().currentUser!!.uid &&
                        it.name.contains(name, true)
            }
        }

    }

    fun onTaskSelected(task: Task){
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
        }
    }

    fun onTaskChecked(task: Task, isChecked: Boolean){
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = isChecked))
        }
    }

    fun onTaskSwiped(task: Task){
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
        }
    }

    fun onUndoDeleteClick(task: Task){
        viewModelScope.launch {
            taskRepository.createTask(task)
        }
    }

    fun onAddNewTaskClick(){
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
        }
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }
    class TasksViewModelFactory(private val repository: TaskRepository,
                                private val savedStateHandle: SavedStateHandle) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TasksViewModel(repository, savedStateHandle) as T
        }
    }
}