package com.example.todoapp.ui.main

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Task

class TasksViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val completed: CheckBox = itemView.findViewById(R.id.check_box_completed)
    private val name: TextView = itemView.findViewById(R.id.text_view_name)
    private val priority: ImageView = itemView.findViewById(R.id.label_priority)

    fun bind(task: Task) {
        completed.isChecked = task.completed
        name.text = task.name
        name.paint.isStrikeThruText = task.completed
        priority.isVisible = task.important
    }
}