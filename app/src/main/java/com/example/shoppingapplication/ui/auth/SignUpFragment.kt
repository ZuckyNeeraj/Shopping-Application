package com.example.shoppingapplication.ui.auth

/**
 * This fragment will deal with the signup of user.
 * It will be loaded over auth activity.
 * Both sign up and login fragment will render over the auth activity.
 * @author Neeraj Mahapatra
 */


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
import com.example.shoppingapplication.databinding.FragmentSignUpBinding
import com.example.shoppingapplication.ui.homepage.HomePageActivity
import com.google.firebase.auth.FirebaseAuth
import com.pranavpandey.android.dynamic.toasts.DynamicToast


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var signUpButton: Button
    private lateinit var signUpUserName: EditText
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        //set up UI elements
        signUpUserName = binding.sinUpUserName
        signUpEmail= binding.signUpEmailId
        signUpPassword = binding.signUpPassword
        signUpButton = binding.signUpButton
        auth = FirebaseAuth.getInstance()

        signUpButtonFunctionality()
        return binding.root
    }

    /**
     * This will collect user name, email & password.
     * it will authenticate using firebase, if everything is correct,
     * it will render the log in fragment
     * @return Log In Fragment
     */
    private fun signUpButtonFunctionality() {
        signUpButton.setOnClickListener {
            val email = signUpEmail.text.toString()
            val password = signUpPassword.text.toString()

            if (email.isEmpty()) {
                activity?.let { DynamicToast.makeError(it, R.string.error_email_empty.toString())
                    .show() }
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                activity?.let { DynamicToast.makeError(it, R.string.error_password_empty.toString())
                    .show() }
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.authFrameFl, LogInFragment())
                            .commit()
                    } else {
                        val errorMsg = when {
                            task.exception?.message?.contains("WEAK_PASSWORD") == true ->
                                R.string.error_weak_password.toString()
                            task.exception?.message?.contains("EMAIL_EXISTS") == true ->
                                R.string.error_user_exists.toString()
                            task.exception?.message?.contains("INVALID_EMAIL") == true ||
                                    task.exception?.message?.contains("MISSING_EMAIL") == true ->
                                R.string.error_auth_failed.toString()
                            else ->
                                task.exception?.message ?: getString(R.string.error_auth_failed)
                        }

                        activity?.let { DynamicToast.makeError(it, errorMsg).show() }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}