package com.example.todoapp.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ItemTaskBinding
import com.example.todoapp.model.Task
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TasksAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val cTask = differ.currentList[position]
                        listener.onItemClick(cTask)
                    }
                }
                checkBoxCompleted.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val cTask = differ.currentList[position]
                        listener.onCheckBoxClick(cTask, checkBoxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    private val callback = object : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.uid == newItem.uid

        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
    val differ: AsyncListDiffer<Task> = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)//LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TasksViewHolder(binding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = differ.currentList[position]
        holder.bind(task)
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }
}