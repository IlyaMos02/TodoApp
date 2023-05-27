package com.example.todoapp.repository

import android.content.ContentValues
import android.util.Log
import com.example.todoapp.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.local.QueryResult

class UserRepository {

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val userDbRef = firestoreDb.collection("users")

    fun createUser(user: User){
        val taskNum = userDbRef.whereEqualTo("user_code", user.user_code).get().result.documents.size

        if(taskNum > 0){
            Log.w(ContentValues.TAG, "This user is already added")
        } else {
            userDbRef.add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }
    }

    companion object{
        var currentUser: User? = null
    }
}

fun mapFromFirebaseUser(user: FirebaseUser) = User(user_code = user.uid)