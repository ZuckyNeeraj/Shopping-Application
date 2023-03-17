package com.example.shoppingapplication.ui.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.FragmentLogInBinding
import com.example.shoppingapplication.databinding.FragmentSignUpBinding
import com.example.shoppingapplication.ui.homepage.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private lateinit var logInButton: Button
    private lateinit var logInEmail: EditText
    private lateinit var logInPassword: EditText
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        //set up UI elements
        logInEmail= binding.logInEmail
        logInPassword = binding.logInPassword
        logInButton = binding.logInButton
        auth = FirebaseAuth.getInstance()


        /**
         * This will check email and password if both are not null,
         * it will authenticate using firebase, if everything is correct,
         * it will render the Home Activity.
         * @return Home Page Activity
         */
        logInButton.setOnClickListener{
            val email: String = logInEmail.text.toString()
            val password: String = logInPassword.text.toString()

            if(email.isEmpty()){
                Toast.makeText(activity,"Email is Empty", Toast.LENGTH_SHORT).show()
            }

            if(password.isEmpty()){
                Toast.makeText(activity,"Password is Empty", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(activity, HomePageActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}