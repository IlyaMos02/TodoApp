package com.example.todoapp.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
data class Task(
    @DocumentId
    var uid: String = "",
    val name: String = "",
    val important: Boolean = false,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis(),
    val user_id: String = ""
) : Parcelable {
    val createdDateFormatted: String
            get() = DateFormat.getDateTimeInstance().format(created)
}