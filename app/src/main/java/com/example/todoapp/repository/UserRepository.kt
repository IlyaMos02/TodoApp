package com.example.todoapp.repository

import android.content.ContentValues
import android.util.Log
import com.example.todoapp.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val userDbRef = firestoreDb.collection("users")

    fun createUser(user: User){
        userDbRef.add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    companion object{
        var currentUser: User? = null
    }
}

fun mapFromFirebaseUser(user: FirebaseUser) = User(user.uid)