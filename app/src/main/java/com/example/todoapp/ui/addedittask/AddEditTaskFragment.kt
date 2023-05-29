package com.example.todoapp.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels() {
        AddEditTaskViewModel.AddEditTaskViewModelFactory(TaskRepository())
    }

    private var currentTask: Task? = null
    private var taskName: String = ""
    private var taskImportance: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditTaskBinding.bind(view)

        val bundle = arguments ?: return
        val args = AddEditTaskFragmentArgs.fromBundle(bundle)

        if(args.task != null){
            currentTask = args.task

            taskName = currentTask!!.name
            taskImportance = currentTask!!.important

            binding.apply {
                editTextTaskName.setText(currentTask!!.name)
                checkBoxImportant.isChecked = currentTask!!.important
                checkBoxImportant.jumpDrawablesToCurrentState()
                textViewDateCreated.isVisible = true
                textViewDateCreated.text = "Created: ${currentTask!!.createdDateFormatted}"
            }
        }

        binding.apply {
            editTextTaskName.addTextChangedListener {
                taskName = it.toString()

            }

            checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                taskImportance = isChecked
            }

            fabSaveTask.setOnClickListener {
                if(currentTask == null)
                    viewModel.onSaveClick(Task(name = taskName, important = taskImportance))
                else
                    viewModel.onSaveClick(currentTask!!.copy(name = taskName, important = taskImportance))
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect{event ->
                when(event){
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }



}