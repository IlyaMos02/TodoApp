package com.example.todoapp.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.ui.task.TasksViewModel

class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels() {
        AddEditTaskViewModel.AddEditTaskViewModelFactory(TaskRepository(), SavedStateHandle())
    }

    private var taskName: String = ""
    private var taskImportance: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditTaskBinding.bind(view)

        val bundle = arguments ?: return
        val args = AddEditTaskFragmentArgs.fromBundle(bundle)

        if(args.task != null){
            binding.apply {
                editTextTaskName.setText(args.task.name)
                checkBoxImportant.isChecked = args.task.important
                checkBoxImportant.jumpDrawablesToCurrentState()
                textViewDateCreated.isVisible = true
                textViewDateCreated.text = "Created: ${args.task.createdDateFormatted}"

                editTextTaskName.addTextChangedListener {
                    taskName = it.toString()

                }

                checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                    taskImportance = isChecked
                }

                fabSaveTask.setOnClickListener {
                    viewModel.onSaveClick(Task(name = taskName, important = taskImportance))
                }
            }
        }
    }



}