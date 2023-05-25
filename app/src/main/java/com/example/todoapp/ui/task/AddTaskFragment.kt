package com.example.todoapp.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    private lateinit var binding:FragmentAddTaskBinding
    private lateinit var listener: DialogSaveBtnClickListener

    fun setListener(listener: DialogSaveBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents() {
        binding.taskSaveBtn.setOnClickListener{
            val taskName = binding.taskEditText.text.toString()
            if(taskName.isNotEmpty()){
                //listener.onSaveTask()
            } else {
                Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface DialogSaveBtnClickListener{

    }

}