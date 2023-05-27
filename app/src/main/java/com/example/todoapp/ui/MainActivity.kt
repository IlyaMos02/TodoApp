package com.example.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todoapp.R
import com.example.todoapp.model.Task
import com.example.todoapp.model.User
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.repository.mapFromFirebaseUser
import com.example.todoapp.ui.login.LoginFragment
import com.example.todoapp.ui.login.OnAuthStateListener
import com.example.todoapp.ui.task.TasksFragment
import com.example.todoapp.utils.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(){
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}