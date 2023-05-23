package com.example.todoapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(container: Int, fragment: Fragment, tag: String = fragment.javaClass.simpleName){
    val transaction = supportFragmentManager
        .beginTransaction()
        .replace(container, fragment, tag)
        .commit()
}

fun SearchView.OnQueryTextChanged(listener: (String) -> Unit){
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