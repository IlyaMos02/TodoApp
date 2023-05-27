package com.example.todoapp.utils

import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

fun Fragment.relaunch(){
    val packageName = requireContext().packageName
    val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(packageName)

    if (launchIntent != null) {
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)
    }

    requireActivity().finish()
}
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
             return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}