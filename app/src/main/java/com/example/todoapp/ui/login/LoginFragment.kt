package com.example.todoapp.ui.login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doLogin()
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val action = LoginFragmentDirections.actionLoginFragment2ToTasksFragment()
            findNavController().navigate(action)
        }
    }

    private var authStateListener: OnAuthStateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnAuthStateListener)
            authStateListener = context
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private fun doLogin(){
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_logo)
            .setAlwaysShowSignInMethodScreen(true)
            .setIsSmartLockEnabled(false)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(res: FirebaseAuthUIAuthenticationResult){
        if(res.resultCode == AppCompatActivity.RESULT_OK){

            FirebaseAuth.getInstance().currentUser?. also {

                val action = LoginFragmentDirections.actionLoginFragment2ToTasksFragment()
                findNavController().navigate(action)
            } ?: Toast.makeText(requireContext(), "Login error", Toast.LENGTH_SHORT).show()

        } else {
            if(res.idpResponse == null) {
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Login error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}