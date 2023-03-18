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


        /**
         * This will collect user name, email & password.
         * it will authenticate using firebase, if everything is correct,
         * it will render the log in fragment
         * @return Log In Fragment
         */
        signUpButton.setOnClickListener {

            val email: String = signUpEmail.text.toString()
            val password: String = signUpPassword.text.toString()

            if(email.isEmpty()){
                activity?.let { it1 -> DynamicToast.makeError(it1,
                    R.string.error_email_empty.toString(), Toast.LENGTH_SHORT).show() }
                return@setOnClickListener
            }

            if(password.isEmpty()){
                activity?.let { it1 -> DynamicToast.makeError(it1, R.string.error_password_empty.toString(), Toast.LENGTH_SHORT).show() }
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.authFrameFl, LogInFragment())
                            .commit()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                        when {
                            task.exception?.message?.contains("WEAK_PASSWORD") == true ->
                                context?.let { it1 ->
                                    DynamicToast.makeWarning(
                                        it1,
                                        R.string.error_weak_password.toString(), Toast.LENGTH_LONG).show()
                                }

                            task.exception?.message?.contains("EMAIL_EXISTS") == true ->
                                context?.let { it1 ->
                                    DynamicToast.makeWarning(
                                        it1,
                                        R.string.error_user_exists.toString(), Toast.LENGTH_LONG).show()
                                }

                            task.exception?.message?.contains("INVALID_EMAIL") == true ||
                                    task.exception?.message?.contains("MISSING_EMAIL") == true ->
                                context?.let { it1 ->
                                    DynamicToast.makeWarning(
                                        it1,
                                        R.string.error_auth_failed.toString(), Toast.LENGTH_LONG).show()
                                }

                            else ->
                                context?.let { it1 -> DynamicToast.makeError(it1, task.exception?.message ?: getString(R.string.error_auth_failed), Toast.LENGTH_LONG).show() }
                        }
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