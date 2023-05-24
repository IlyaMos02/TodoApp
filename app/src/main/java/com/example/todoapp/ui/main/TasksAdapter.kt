package com.example.todoapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TasksAdapter(
    /*private val options: FirestoreRecyclerOptions<Task>,*/
    private val onItemClickListener: OnItemClickListener
)/* : FirestoreRecyclerAdapter<Task, TasksViewHolder>(options)*/ {


    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val holder = TasksViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_task,
                parent,
                false
            )
        )

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(
                //options.snapshots[holder.absoluteAdapterPosition]
            )
        }

        return holder
    }*/

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int, task: Task) {
        holder.bind(task)
    }

    override fun onDataChanged() {

    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
    }
}