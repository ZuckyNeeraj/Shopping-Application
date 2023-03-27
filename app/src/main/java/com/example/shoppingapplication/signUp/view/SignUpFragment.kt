package com.example.shoppingapplication.signUp.view

/**
 * This fragment will deal with the signup of user.
 * It will be loaded over auth activity.
 * Both sign up and login fragment will render over the auth activity.
 * @author Neeraj Mahapatra
 */


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.shoppingapplication.R
import com.example.shoppingapplication.databinding.FragmentSignUpBinding
import com.example.shoppingapplication.login.view.LogInFragment
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

    private val onClickListener = View.OnClickListener { view ->
        when (view) {
            binding.signUpButton -> {
                val email = signUpEmail.text.toString()
                val password = signUpPassword.text.toString()

                if (email.isEmpty()) {
                    activity?.let {
                        DynamicToast.makeError(it, R.string.error_email_empty.toString())
                            .show()
                    }
                }

                if (password.isEmpty()) {
                    activity?.let {
                        DynamicToast.makeError(it, R.string.error_password_empty.toString())
                            .show()
                    }
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
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inits()
        disableSignUpButtonAtFirst()
        setListeners()
    }

    private fun inits() {
        signUpUserName = binding.sinUpUserName
        signUpEmail = binding.signUpEmailId
        signUpPassword = binding.signUpPassword
        signUpButton = binding.signUpButton
        auth = FirebaseAuth.getInstance()
    }

    private fun setListeners() {
        binding.signUpButton.setOnClickListener(onClickListener)
    }

    /**
     * This method will hide the signup button until name, email & password are not filled
     * @return null
     */
    private fun disableSignUpButtonAtFirst() {

// Disable the SignUpButton initially
        signUpButton.isEnabled = false

// Add text change listeners to each EditText field
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if all three EditText fields are not empty
                signUpButton.isEnabled = (!signUpUserName.text.isNullOrEmpty()
                        && !signUpEmail.text.isNullOrEmpty()
                        && !signUpPassword.text.isNullOrEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        signUpUserName.addTextChangedListener(textWatcher)
        signUpEmail.addTextChangedListener(textWatcher)
        signUpPassword.addTextChangedListener(textWatcher)

    }

    /**
     * This will collect user name, email & password.
     * it will authenticate using firebase, if everything is correct,
     * it will render the log in fragment
     * @return Log In Fragment
     */


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}