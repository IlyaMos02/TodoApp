package com.example.todoapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

class TaskRepository {

    private var tasks = MutableLiveData<List<Task>>()
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val taskDbRef = firestoreDb.collection("tasks")

    fun getT() = taskDbRef.get().addOnSuccessListener {result ->
        tasks.postValue(result.toObjects<Task>())
    }
        .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }
    fun getTasks() = taskDbRef.orderBy("important", Query.Direction.DESCENDING)

    fun getTasks(userId: String) = getTasks().whereEqualTo("user_id", userId)

    fun getTasks(userId: String, name: String) = taskDbRef.whereGreaterThanOrEqualTo("name", name)
        .whereEqualTo("user_id", userId)

    fun createTask(task: Task){
        taskDbRef.add(task)
    }

    fun editTask(task: Task){
        taskDbRef.document(task.uid).set(task)
    }

    /*fun getTasks() /*: Flow<List<Task>>*/ = taskDbRef.orderByChild("important")

    fun getTasks(userId: String) = taskDbRef.orderByChild("user_id").equalTo(userId)

    fun getTasks(userId: String, name:String) = taskDbRef.startAt(name)

    fun createTask(task: Task){
        task.uid = taskDbRef.push().key.toString()

        val taskNodeRef = taskDbRef.child(task.uid)
        taskNodeRef.setValue(task)
    }

    fun updateTask(task: Task){
        val taskNodeRef = taskDbRef.child(task.uid)

        taskNodeRef.setValue(task)
    }

    fun deleteTask(task: Task){
        val taskNodeRef = taskDbRef.child(task.uid)

        taskNodeRef.removeValue()
    }*/
}