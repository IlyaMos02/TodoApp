package com.example.todoapp.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class TaskRepository {

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val taskDbRef = firestoreDb.collection("tasks")

    var tasks = MutableLiveData<List<Task>>()

    init {
        getTasks()
    }

    fun getT(): List<Task> {
        val result = taskDbRef.orderBy("important", Query.Direction.DESCENDING)
            .get().result.toObjects<Task>()
        return result
    }

    fun getTasks() = taskDbRef.orderBy("important", Query.Direction.DESCENDING)
        .get().addOnSuccessListener {result ->
            tasks.postValue(result.toObjects<Task>())
        } .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }

    fun createTask(task: Task){
        taskDbRef.add(task)
            .addOnSuccessListener { Log.d("CrS", "Create success") }
            .addOnFailureListener { Log.e("Err", it.message.toString()) }
    }

    fun updateTask(task: Task){
        taskDbRef.document(task.uid).set(task)
            .addOnSuccessListener { Log.d("UpS", "Update success") }
            .addOnFailureListener { Log.e("Err", it.message.toString()) }
    }

    fun deleteTask(task: Task){
        taskDbRef.document(task.uid).delete()
            .addOnSuccessListener { Log.d("DelS", "Delete success") }
            .addOnFailureListener { Log.e("Err", it.message.toString()) }
    }

}