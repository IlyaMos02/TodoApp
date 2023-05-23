package com.example.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.R
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.repository.mapFromFirebaseUser
import com.example.todoapp.ui.login.LoginFragment
import com.example.todoapp.ui.login.OnAuthStateListener
import com.example.todoapp.ui.main.TasksFragment
import com.example.todoapp.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), OnAuthStateListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFragment(){
        val user = FirebaseAuth.getInstance().currentUser

        if(user == null){
            replaceFragment(R.id.fragmentContainer, LoginFragment())
        } else {
            UserRepository.currentUser = mapFromFirebaseUser(user)
            replaceFragment(R.id.fragmentContainer, TasksFragment())
        }
    }

    override fun onAuthStateChanged() {
        showFragment()
    }
}