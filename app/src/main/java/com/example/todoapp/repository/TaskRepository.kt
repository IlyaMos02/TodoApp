package com.example.todoapp.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.todoapp.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TaskRepository {

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val taskDbRef = firestoreDb.collection("tasks")

    /*private val database = Firebase.database
    private val tasksDbRef = database.getReference("tasks")*/

    fun getTasks() = taskDbRef.orderBy("important", Query.Direction.DESCENDING)

    fun getTasks(userId: String) = getTasks().whereEqualTo("user_id", userId)

    fun getTasks(userId: String, name:String) = getTasks(userId).whereGreaterThanOrEqualTo("name", name)

    fun createTask(task: Task){
        taskDbRef.add(task)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
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