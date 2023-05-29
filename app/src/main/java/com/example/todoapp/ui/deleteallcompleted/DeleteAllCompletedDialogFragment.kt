package com.example.todoapp.ui.deleteallcompleted

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.repository.TaskRepository

class DeleteAllCompletedDialogFragment : DialogFragment() {

    private val viewModel: DeleteAllCompletedViewModel by viewModels() {
        DeleteAllCompletedViewModel.DeleteAllCompletedViewModelFactory(TaskRepository(), lifecycleScope)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you realy want to delete all completed tasks?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){ _, _ ->
                viewModel.onConfirmClick()
            }
            .create()
}