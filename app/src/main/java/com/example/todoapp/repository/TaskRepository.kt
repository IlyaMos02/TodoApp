package com.example.todoapp.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class TaskRepository {

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val taskDbRef = firestoreDb.collection("tasks")
    private val generalQuery = taskDbRef.whereEqualTo("user_id", FirebaseAuth.getInstance().currentUser!!.uid)

    val tasks = MutableLiveData<List<Task>>()

    init {
        getTasksSortedByImportance()
        listenToTasks()
    }

    private fun listenToTasks() {
        taskDbRef.addSnapshotListener { value, error ->
            if(error != null){
                Log.w(TAG, "Listen failed", error)
                return@addSnapshotListener
            }

            if(value != null){
                val result = value.toObjects<Task>()

                tasks.postValue(result.filter {
                    it.user_id == FirebaseAuth.getInstance().currentUser!!.uid
                })
            }
        }
    }

    fun getTasksSortedByImportance() = generalQuery.orderBy("important", Query.Direction.DESCENDING)
        .get().addOnSuccessListener {result ->
            tasks.postValue(result.toObjects<Task>())
        } .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }

    fun getTasksSortedByDate() = generalQuery.orderBy("created", Query.Direction.DESCENDING)
        .get().addOnSuccessListener {result ->
            tasks.postValue(result.toObjects<Task>())
        } .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }

    fun getTasksSortedByName() = generalQuery.orderBy("name", Query.Direction.ASCENDING)
        .get().addOnSuccessListener {result ->
            tasks.postValue(result.toObjects<Task>())
        } .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }

    fun getTasksByName(name: String) = generalQuery.orderBy("important", Query.Direction.DESCENDING)
        .get().addOnSuccessListener { result ->
            val list = result.toObjects<Task>()
            tasks.postValue(list.filter { it.name.contains(name) })
        } .addOnFailureListener {
            Log.e("Err", it.message.toString())
        }

    fun getTasksByHideCompleted() = generalQuery.orderBy("important", Query.Direction.DESCENDING)
        .whereEqualTo("completed", false).get()
        .addOnSuccessListener {result ->
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

    fun deleteAllCompletedTasks(){
        generalQuery.whereEqualTo("completed", true).get()
            .addOnSuccessListener {result ->
                for(document in result)
                    taskDbRef.document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("DelS", "All completed tasks deleted")
                        }
            }
            .addOnFailureListener {
                Log.w("DelF", "Completed tasks not found")
            }
    }

}