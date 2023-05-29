package com.example.todoapp.ui.task

import androidx.lifecycle.*
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.ADD_TASK_RESULT_OK
import com.example.todoapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel(private val taskRepository: TaskRepository
                     ) : ViewModel() {
    val tasksLiveData = taskRepository.tasks

    var searchQuery: String = ""
    var hideCompleted = false

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    fun onTaskSearched(searchStr: String) {
        taskRepository.getTasksByName(searchStr)
    }

    fun onSortTasksByDate(){
        viewModelScope.launch {
            taskRepository.getTasksSortedByDate()
        }
    }

    fun onSortTasksByName(){
        viewModelScope.launch {
            taskRepository.getTasksSortedByName()
        }
    }

    fun onSortTasksByImportance(){
        viewModelScope.launch {
            taskRepository.getTasksSortedByImportance()
        }
    }

    fun onHideAllCompleted(){
        viewModelScope.launch {
            taskRepository.getTasksByHideCompleted()
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

    fun onAddEditResult(result: Int){
        when(result){
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    fun onDeleteAllCompletedClick(){
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
        }
    }

    private fun showTaskSavedConfirmationMessage(msg: String) {
        viewModelScope.launch {
            tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(msg))
        }
    }


    companion object{
        val SORT_BY_NAME = "Sort by name"
        val SORT_BY_DATE_CREATED = "Sort by date created"
        val SORT_BY_IMPORTANCE = "Sort by importance"
        val HIDE_COMPLETED = "Hide completed"
        val DELETE_ALL_COMPLETED = "Delete all completed"
        val EXIT_FROM_ACCOUNT = "Exit"
    }
    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen: TasksEvent()
    }
    class TasksViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TasksViewModel(repository) as T
        }
    }
}