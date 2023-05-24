package com.example.todoapp.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    @DocumentId
    val uid: String = "",
    val user_code: String = ""
) : Parcelable